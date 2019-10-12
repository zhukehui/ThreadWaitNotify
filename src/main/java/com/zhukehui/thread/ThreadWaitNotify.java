package com.zhukehui.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Information{ //资源类
	
	private int number = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	public void increment() throws Exception {
		lock.lock();//加锁
		try {
			//判断
			while (number != 0) {
				//this.wait();
				condition.await();//等候
			}
			//干活
			number++;
			System.out.println(Thread.currentThread().getName()+"\t"+number);
			//通知
			//this.notifyAll();
			condition.signalAll();//唤醒
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();//释放
		}
	}
	
	public void decrement() throws Exception {
		lock.lock();;//加锁
		try {
			//判断
			while (number == 0) {
				//this.wait();
				condition.await();//等候
			}
			//干活
			number--;
			System.out.println(Thread.currentThread().getName()+"\t"+number);
			//通知
			//this.notifyAll();
			condition.signalAll();//唤醒
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();;//释放
		}
	}
	
	
	
	/*
	 * public synchronized void increment() throws Exception {
	 * 
	 * //判断 while (number!=0) { this.wait();//等候} //干活 number++;
	 * System.out.println(Thread.currentThread().getName()+"\t"+number); //通知
	 * this.notifyAll();//唤醒 }
	 * 
	 * public synchronized void decrement() throws Exception {
	 * 
	 * //判断 while (number == 0) { this.wait();//等候} //干活 number--;
	 * System.out.println(Thread.currentThread().getName()+"\t"+number); //通知
	 * this.notifyAll();//唤醒 }
	 */
	
}

/**
 *
 * 实现两个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 实现交替，来10轮，变量初始值为零。
 *
 * 1    高聚低合前提下，线程操作资源类
 * 2    判断/干活/通知
 * 3    小心，防止多线程的虚假唤醒,判断时候用while而不是if
 * 4
 *
 *
 */

public class ThreadWaitNotify {
	public static void main(String[] args) {
		
		Information information = new Information();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				try {
					Thread.sleep(400);
					information.increment();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "A").start();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				try {
					TimeUnit.MILLISECONDS.sleep(5);
					information.decrement();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "B").start();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				try {
					Thread.sleep(400);
					information.increment();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "C").start();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				try {
					TimeUnit.MILLISECONDS.sleep(5);
					information.decrement();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "D").start();
		
	}
}
