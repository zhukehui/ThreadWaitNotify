package com.zhukehui.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Resources{ //资源类
	
	private int flag = 1 ; //定以一个标志位，A：1，B:2，C:3
	private Lock lock = new ReentrantLock();
	private Condition condition1 = lock.newCondition();
	private Condition condition2 = lock.newCondition();
	private Condition condition3 = lock.newCondition();
	
	public void print5() {
		lock.lock();//加锁
		try {
			//判断
			while (flag != 1) {
				condition1.await();//等候
			}
			//干活
			for (int i = 1; i <= 5; i++) {
				System.out.println(Thread.currentThread().getName()+"\t"+i);
			}
			//通知
			flag = 2 ;
			condition2.signal();//唤醒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();//解锁
		}
	}
	
	public void print10() {
		lock.lock();//加锁
		try {
			//判断
			while(flag != 2) {
				condition2.await();//等候
			}
			//干活
			for (int i = 1; i <=10;i++) {
				System.out.println(Thread.currentThread().getName()+"\t"+i);
			}
			//通知
			flag = 3;
			condition3.signal();//唤醒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();//解锁
		}
	}
	
	public void print15() {
		lock.lock();//加锁
		
		try {
			//判断
			while(flag != 3) {
				condition3.await();//等候
			}
			//干活
			for (int i = 1; i <= 15; i++) {
				System.out.println(Thread.currentThread().getName()+"\t"+i);
			}
			//通知
			flag = 1;
			condition1.signal();//唤醒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();//解锁
		}
	}
}

/**
 *
 * 多线程之间按顺序调用，实现A->B->C
 * 三个线程启动，要求如下：
 *
 * AA打印5次，BB打印10次，CC打印15次
 * 接着
 * AA打印5次，BB打印10次，CC打印15次
 * ......来10轮
 *
 * 1    高聚低合前提下，线程操作资源类
 * 2    判断/干活/通知
 * 3    多线程交互中，必须要防止多线程的虚假唤醒，也即（判断只用while，不能用if）
 * 4    注意判断标志位的更新
 */

public class ThreadSequenceCalling {
	public static void main(String[] args) {
		Resources resources = new Resources();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				resources.print5();
			}
		},"A").start();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				resources.print10();
			}
		},"B").start();
		
		new Thread(() -> {
			for (int i = 0; i <10; i++) {
				resources.print15();
			}
		},"C").start();
		
	}
}
