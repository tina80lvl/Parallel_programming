package fgbank;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

/**
 * Bank implementation.
 *
 * <p>:
 */
public class BankImpl implements Bank {
    /**
     * An array of accounts by index.
     */
    private final Account[] accounts;
    /**
     * An array of locks of accounts.
     */
    private final Lock[] locks;

    /**
     * Creates new bank instance.
     * @param n the number of accounts (numbered from 0 to n-1).
     */
    public BankImpl(int n) {
        accounts = new Account[n];
        locks = new Lock[n];
        for (int i = 0; i < n; i++) {
            accounts[i] = new Account();
            locks[i] = new ReentrantLock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfAccounts() {
        return accounts.length;
    }

    /**
     * {@inheritDoc}
     * <p>:
     */
    @Override
    public long getAmount(int index) {
        locks[index].lock();
        try {
            return accounts[index].amount;
        } finally {
            locks[index].unlock();
        }
    }

    /**
     * {@inheritDoc}
     * <p>:
     */
    @Override
    public long getTotalAmount() {
        for (int cur = 0; cur < getNumberOfAccounts(); cur++) {
            locks[cur].lock();
        }
        try {
            long sum = 0;
            for (Account account : accounts) {
                sum += account.amount;
            }
            return sum;
        } finally {
            for (int cur = 0; cur < getNumberOfAccounts(); cur++) {
                locks[cur].unlock();
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>:
     */
    @Override
    public long deposit(int index, long amount) {
        locks[index].lock();
        try {
            if (amount <= 0)
                throw new IllegalArgumentException("Invalid amount: " + amount);
            Account account = accounts[index];
            if (amount > MAX_AMOUNT || account.amount + amount > MAX_AMOUNT)
                throw new IllegalStateException("Overflow");
            account.amount += amount;
            return account.amount;
        } finally {
            locks[index].unlock();
        }
    }

    /**
     * {@inheritDoc}
     * <p>:
     */
    @Override
    public long withdraw(int index, long amount) {
        locks[index].lock();
        try {
            if (amount <= 0)
                throw new IllegalArgumentException("Invalid amount: " + amount);
            Account account = accounts[index];
            if (account.amount - amount < 0)
                throw new IllegalStateException("Underflow");
            account.amount -= amount;
            return account.amount;
        } finally {
            locks[index].unlock();
        }
    }

    /**
     * {@inheritDoc}
     * <p>:
     */
    @Override
    public void transfer(int fromIndex, int toIndex, long amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid amount: " + amount);
        if (fromIndex == toIndex)
            throw new IllegalArgumentException("fromIndex == toIndex");
        int minIndex = Math.min(fromIndex, toIndex);
        int maxIndex = Math.max(fromIndex, toIndex);
        locks[minIndex].lock();
        locks[maxIndex].lock();
        try {
            Account from = accounts[fromIndex];
            Account to = accounts[toIndex];
            if (amount > from.amount)
                throw new IllegalStateException("Underflow");
            else if (amount > MAX_AMOUNT || to.amount + amount > MAX_AMOUNT)
                throw new IllegalStateException("Overflow");
            from.amount -= amount;
            to.amount += amount;
        } finally {
            locks[maxIndex].unlock();
            locks[minIndex].unlock();
        }
    }

    /**
     * Private account data structure.
     */
    private static class Account {
        /**
         * Amount of funds in this account.
         */
        long amount;
    }
}
