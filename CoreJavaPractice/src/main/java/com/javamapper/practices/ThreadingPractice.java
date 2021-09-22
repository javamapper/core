package com.javamapper.practices;

import static com.javamapper.common.AppConstants.ERROR_LOG;
import static com.javamapper.common.AppConstants.READ_VALUE_LOG;
import static com.javamapper.common.AppConstants.REMOVE_INT;
import static com.javamapper.common.AppConstants.WRITE_INT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javamapper.common.AppConstants.PAYMENT_TYPE;
import com.javamapper.common.SharedProcessor;
import com.javamapper.common.Utility;
import com.javamapper.entity.BankingBalance;
import com.javamapper.entity.Customer;
import com.javamapper.entity.DataStream;
import com.javamapper.entity.EvenTask;
import com.javamapper.entity.FileActionReadWrite;
import com.javamapper.entity.FileActionStamped;
import com.javamapper.entity.OddTask;

public class ThreadingPractice {
	private static Logger logger = LoggerFactory.getLogger(ThreadingPractice.class);
	static final Callable<Integer> taskCallable1NoSleepLogTime = () -> {
		logger.info("Task-taskWithTime executed on : {}", LocalDateTime.now());
		return 1;
	};
	static final Callable<Integer> taskCallable1Sleep1Sec = () -> {
		try {
			logger.info("task 1 job processing..");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 1;
	};
	static final Callable<Integer> taskCallable2Sleep1Sec = () -> {
		try {
			logger.info("task 2 job processing..");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 2;
	};
	static final Callable<Integer> taskCallable1Sleep3Sec = () -> {
		try {
			logger.info("task 3 job processing..");
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 3;
	};
	static final Callable<Integer> taskCallable4Sleep1Sec = () -> {
		try {
			logger.info("task 4 job processing..");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 4;
	};
	static final Callable<Integer> taskCallable5Sleep1Sec = () -> {
		try {
			logger.info("task 5 job processing..");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 5;
	};
	static final Callable<Integer> taskCallable1LogTime = () -> {
		try {
			logger.info("Task-taskWithTime executed on : {}", LocalDateTime.now());
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 1;
	};

	static final Callable<Integer> taskCallable5Sleep5Sec = () -> {
		try {
			logger.info("taskTake5Sec job processing..");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		return 5;
	};
	static final Runnable taskRunnable1Sleep1Sec = () -> {
		try {
			logger.info("taskWithoutReturn1 job processing..");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	};
	static final Runnable taskRunnable2Sleep1Sec = () -> {
		try {
			logger.info("taskWithoutReturn2 job processing..");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	};

	static final Runnable taskRunnable1LogTime = () -> {
		try {
			logger.info("Task-taskRunnable1LogTime executed on : {}", LocalDateTime.now());
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	};

	static final Runnable taskRunnable2LogTime = () -> {
		try {
			logger.info("Task-taskRunnable2LogTime executed on : {}", LocalDateTime.now());
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	};

	public void basicThreadCreation() {
		Thread currentThread = Thread.currentThread();
		logger.info("--{} job start thread", currentThread.getName());
		logger.info("--{} job in process", currentThread.getName());
		Thread th = new Thread(() -> {
			Thread childTh = Thread.currentThread();
			try {
				currentThread.join();
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
			logger.info("--{} job start thread", childTh.getName());
			logger.info("--{} job in process", childTh.getName());
			logger.info("--{} job end thread", childTh.getName());
		});
		th.setName("child-thread");
		th.start();

		logger.info("--{} job end thread", currentThread.getName());
	}

	/**
	 * Single object thread communication.
	 */
	public void singleObjectThreadCommunication() {
		DataStream dataStream = new DataStream();
		// Thread 1- Producer
		Thread producerThread = new Thread(() -> {
			List<String> dataSet = Arrays.asList("streamBegin", "stream1", "stream2", "stream3", "stream4", "stream5",
					"stream6", "stream7", "stream8", "streamEnd");
			for (String dataRef : dataSet) {
				logger.info("Produce : {}", dataRef);
				dataStream.send(dataRef);
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
				} catch (InterruptedException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
			}
		});
		// Thread 2- Consumer
		Thread consumerThread = new Thread(() -> {
			String currentState = dataStream.receive();
			do {
				logger.info("Consume : {}", currentState);
				currentState = dataStream.receive();
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
				} catch (InterruptedException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
			} while (!"streamEnd".equals(currentState));

		});
		producerThread.start();
		consumerThread.start();
		try {
			producerThread.join();
			consumerThread.join();
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Inter thread communcation using blocking queue.
	 */
	public void interThreadCommuncationUsingBlockingQueue() {

		BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

		try {
			// Producer 1
			Thread producerThread1 = new Thread(Utility.getProducerBlockingQueue(queue, 0, "producer1", 1000));
			// Producer 2
			Thread producerThread2 = new Thread(Utility.getProducerBlockingQueue(queue, 100, "producer2", 2000));
			// Consumer 1
			Thread consumerThread1 = new Thread(Utility.getConsumerBlockingQueue(queue, "consumer1", 1000));
			// Consumer 2
			Thread consumerThread2 = new Thread(Utility.getConsumerBlockingQueue(queue, "consumer1", 1000));

			producerThread1.start();
			producerThread2.start();
			consumerThread1.start();
			consumerThread2.start();
		} catch (Exception e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Even odd print by two seprate thread.
	 */
	public void evenOddPrintByTwoSeprateThread() {
		SharedProcessor sharedProcessor = new SharedProcessor();
		Thread oddThread = new OddTask(20, sharedProcessor);
		Thread evenThread = new EvenTask(20, sharedProcessor);
		oddThread.start();
		evenThread.start();
	}

	/**
	 * Interrupting thread.
	 */
	public void interruptingThread() {
		Thread waitingThread = new Thread(() -> {
			try {
				/**
				 * If not using sleep but still calling interrupt then no use of call, because
				 * thread never went to waiting state
				 */
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
				/**
				 * Not allow to process child thread then throw throw new
				 * RuntimeException("Blocker waitingThread Thread interrupted."+e);
				 */
			}
			logger.info("waitingThread continue process .. ");
		});
		waitingThread.start();
		waitingThread.interrupt();
		logger.info("continue process .. ");
		Stream.iterate(0, n -> n + 1).limit(100).forEach(n -> logger.info("{}", n));
	}

	public void diffInRunCallAndThreadStart() {
		Thread th = new Thread(() -> logger.info("task called"), "child");
		// When calling run method then it is just a method call on same thread
		th.run();
		// Thread Scheduler take-care it to process
		th.start();
	}

	public void joinExample() {
		Thread th1 = new Thread(() -> {
			logger.info("task-1 called");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		}, "task1");
		Thread th2 = new Thread(() -> logger.info("task-2 called"), "task2");
		Thread th3 = new Thread(() -> logger.info("task-3 called"), "task3");

		th1.start();
		try {
			// Running/Current(main) thread will wait to complete task1 Then processed to
			// next line
			th1.join();
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		th2.start();
		th3.start();
	}

	public void threadPriorityExample() {
		// MIN-1, NORMAL-5 AND MAX-10
		Thread th1 = new Thread(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
			logger.info("task-1 called");
		}, "task1");
		Thread th2 = new Thread(() -> logger.info("task-2 called"), "task2");
		Thread th3 = new Thread(() -> logger.info("task-3 called"), "task3");
		th3.setPriority(10);
		th1.start();
		th2.start();
		th3.start();
		/*
		 * Result As Priority high for th3 so task-3 start early compare to task-2
		 * 21/09/03 13:21:33 INFO task3 practices.ThreadingPractice: task-3 called
		 * 21/09/03 13:21:33 INFO task2 practices.ThreadingPractice: task-2 called
		 * 21/09/03 13:21:43 INFO task1 practices.ThreadingPractice: task-1 called
		 */
	}

	public List<String> getThreadStatus() {
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		List<String> status = new ArrayList<>();
		for (Thread t : threads) {
			String name = t.getName();
			Thread.State state = t.getState();
			int priority = t.getPriority();
			String type = t.isDaemon() ? "Daemon" : "Normal";
			status.add(String.format("%-20s \t %s \t %d \t %s", name, state, priority, type));
		}
		return status;
	}

	public boolean isWaitDeamon(Thread... threads) {
		return Stream.of(threads).anyMatch(Thread::isAlive);
	}

	public void daemonThreadExample() {
		Thread th2 = new Thread(() -> {
			logger.info("task-2 called");
			Stream.iterate(0, n -> n + 1).limit(10).forEach(n -> {
				try {
					Thread.sleep(1000);
					logger.info("Task-2 Working..");
				} catch (InterruptedException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
			});
		}, "task-2");
		Thread th3 = new Thread(() -> {
			logger.info("task-2 called");
			Stream.iterate(0, n -> n + 1).limit(10).forEach(n -> {
				try {
					Thread.sleep(2000);
					logger.info("Task-3 Working..");
				} catch (InterruptedException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
			});
		}, "task-2");
		Thread th1 = new Thread(() -> {
			logger.info("daemon thread started");
			while (isWaitDeamon(th2, th3)) {
				try {
					Thread.currentThread();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				} finally {
					logger.info("daemon logging -> finally running thread status {}", getThreadStatus());
				}
			}
		}, "daemon");
		th1.setDaemon(false);
		th1.start();
		th2.start();
		th3.start();
	}

	/**
	 * 1) New single thread executor example.
	 */
	public void newSingleThreadExecutorExample() {
		// Executes only one thread
		ExecutorService es = Executors.newSingleThreadExecutor();
		Future<String> submit = es.submit(taskRunnable1Sleep1Sec, "Complete");
		while (!submit.isDone()) {
			try {
				logger.info("Return value: {}", submit.get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Inter thread comm thread pool blocking queue.
	 */
	public void interThreadCommThreadPoolBlockingQueue() {
		BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
		ExecutorService executor = Executors.newFixedThreadPool(2);

		Runnable producer = () -> {
			int value = 0;
			try {
				while (true) {
					blockingQueue.put(++value);
					logger.info("Producer produced:-{}", value);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		};
		Runnable consumer = () -> {
			try {
				while (true) {
					Integer value = blockingQueue.take();
					logger.info("Consumer consumed:-{}", value);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		};
		executor.execute(producer);
		executor.execute(consumer);
		executor.shutdown();
	}

	/***
	 * Executor AND ExecutorService both are interface Executors is Factory/utility
	 * class Executors use for Executor, ExecutorService, ScheduledExecutorService,
	 * ThreadFactory, and Callable classes 2)
	 */
	public void newFixedThreadPoolLimitedTaskExample() {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			/**
			 * submit is method of ExecutorService interface Take Callable object in
			 * argument,In another overload method we pass Runnable with another argument of
			 * return value
			 */
			Integer returnObject = 5;
			Future<Integer> task1Return = executorService.submit(taskCallable1Sleep1Sec);
			/**
			 * returnObject just to pass value when task completed
			 */
			Future<Integer> taskWithoutReturn1Return = executorService.submit(taskRunnable1Sleep1Sec, returnObject);
			/**
			 * If don't want to take any value in return then we use like below..
			 */
			Future<Void> taskWithoutReturn2Return = executorService.submit(taskRunnable2Sleep1Sec, null);
			Future<Integer> task3Return = executorService.submit(taskCallable5Sleep5Sec);
			/**
			 * execute is method of Executor interface Take Runnable object in argument
			 */
			executorService.execute(taskRunnable1Sleep1Sec);
			executorService.shutdown();
			if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
			logger.info("task1Return:{} taskWithoutReturn1:{} taskWithoutReturn2:{} taskTake5Sec:{}", task1Return.get(),
					taskWithoutReturn1Return.get(), taskWithoutReturn2Return.get(), task3Return.get());
		} catch (InterruptedException | ExecutionException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 
	 * 3) newScheduledThreadPool.
	 */
	public void newScheduledThreadPoolExample() {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
		executorService.scheduleWithFixedDelay(taskRunnable1LogTime, 2L, 5L, TimeUnit.SECONDS);// Delay always fixed
																								// 5sec | initial delay
																								// 2Sec
		executorService.scheduleAtFixedRate(taskRunnable2LogTime, 2L, 10L, TimeUnit.SECONDS);// After each 10Sec task
																								// execute | initial
																								// delay 2Sec

		ScheduledFuture<Integer> returnGet = executorService.schedule(taskCallable1LogTime, 5, TimeUnit.SECONDS);// schedule
																													// task
																													// after
																													// 5sec
		try {
			logger.info("Return value: {}", returnGet.get());
			// executor shutdown after 20 seconds
			TimeUnit.SECONDS.sleep(20);
			executorService.shutdown();
			executorService.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * New fixed thread pool example.
	 */
	public void newFixedThreadPoolExample() {
		/**
		 * All below task will complete by 3 threads
		 */
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			List<Future<Integer>> futures = executorService.invokeAll(List.of(taskCallable1Sleep1Sec,
					taskCallable2Sleep1Sec, taskCallable1Sleep3Sec, taskCallable4Sleep1Sec, taskCallable5Sleep1Sec));
			futures.stream().map(future -> {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
				return 0;
			}).forEach(val -> logger.info("task return value: {}", val));
			executorService.execute(taskRunnable1Sleep1Sec);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		executorService.shutdown();
	}

	/**
	 * New fixed thread pool time out example.
	 */
	public void newFixedThreadPoolTimeOutExample() {
		/**
		 * All below task will complete by 3 threads
		 */
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			List<Future<Integer>> futures = executorService.invokeAll(
					List.of(taskCallable1Sleep1Sec, taskCallable2Sleep1Sec, taskCallable1Sleep3Sec), 2,
					TimeUnit.SECONDS);
			futures.stream().map(future -> {
				try {
					return future.get(2, TimeUnit.SECONDS);
				} catch (Exception e) { // Handle: java.util.concurrent.CancellationException :: So return 0 apply
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
				return 0;
			}).forEach(val -> logger.info(" task return value: {}", val));

		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		logger.info("Continue processing.");
		executorService.shutdown();
	}

	/**
	 * Thread pool executor example.
	 */
	public void threadPoolExecutorExample() {
		/**
		 * ThreadPoolExecutor class that implements both interfaces(Executor,
		 * ExecutorService).
		 */
		ExecutorService executorService = new ThreadPoolExecutor(10, 50, 5L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>());
		try {
			List<Future<Integer>> futures = executorService.invokeAll(
					List.of(taskCallable1Sleep1Sec, taskCallable2Sleep1Sec, taskCallable1Sleep3Sec), 2,
					TimeUnit.SECONDS);

			futures.stream().map(future -> {
				try {
					return future.get(2, TimeUnit.SECONDS);
				} catch (Exception e) { // Handle: java.util.concurrent.CancellationException :: So return 0 apply
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				}
				return 0;
			}).forEach(val -> logger.info(" task return value: {}", val));

		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		logger.info("Continue processing..");
		executorService.shutdown();
	}

	/**
	 * Thread too many job.
	 */
	public void threadTooManyJob() {
		ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 50, 5L, TimeUnit.HOURS,
				new LinkedBlockingQueue<>());
		Stream.iterate(0, n -> n + 1).limit(100).forEach(n -> {
			try {
				Future<Integer> submit = executorService.submit(taskCallable1NoSleepLogTime);
				logger.info("Return value: {}", submit.get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		});
		executorService.shutdown();
	}

	/**
	 * Thread pool executor with limited queue size example.
	 */
	public void threadPoolExecutorWithLimitedQueueSizeExample() {
		ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 3, 1, TimeUnit.HOURS,
				new ArrayBlockingQueue<>(10, false));
		Semaphore semaphore = new Semaphore(10); // equal to queue capacity
		List<Pair<String, Integer>> result = new CopyOnWriteArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(100).forEach(n -> {
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e1));
				Thread.currentThread().interrupt();
			}
			executorService.submit(() -> {
				try {
					logger.info(" ----- Queue current capacity:{}", executorService.getQueue().size());
					result.add(Pair.of(Thread.currentThread().getName(), taskCallable1NoSleepLogTime.call()));
				} catch (Exception e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				} finally {
					semaphore.release();
				}
			});
		});
		result.stream().forEach(val -> logger.info("Return value: {}", val));
		logger.info("Continue processing..");
		executorService.shutdown();
	}

	/***
	 * A semaphore controls access to a shared resource by using permits. semaphore
	 * allow access to shared resource : permits are greater than zero. semaphore
	 * does not allow access to shared resource : permits are zero or less than
	 * zero.
	 *
	 * semaphore(int permits) // initial number, If it negative it means first
	 * semaphore.releases must occur before any semaphore.acquires semaphore(int
	 * permits,boolean fair) //fair : True -> when waiting threads are granted a
	 * permit in the order in which they requested access
	 *
	 * acquire & release work to share resource
	 */
	public void semaphoreUseExample() {
		class Writer implements Runnable {
			Semaphore writerSemaphore;
			Semaphore readerSemaphore;

			public Writer(Semaphore readerSemaphore, Semaphore writerSemaphore) {
				this.readerSemaphore = readerSemaphore;
				this.writerSemaphore = writerSemaphore;
			}

			public void run() {
				Stream.iterate(0, n -> n + 1).limit(30).forEach(n -> {
					try {
						writerSemaphore.acquire();
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
						Thread.currentThread().interrupt();
					}
					logger.info("Writer writing ... {}", n);
					readerSemaphore.release();
				});
			}
		}
		class Reader implements Runnable {
			Semaphore writerSemaphore;
			Semaphore readerSemaphore;

			public Reader(Semaphore readerSemaphore, Semaphore writerSemaphore) {
				this.readerSemaphore = readerSemaphore;
				this.writerSemaphore = writerSemaphore;
			}

			public void run() {
				Stream.iterate(0, n -> n + 1).limit(30).forEach(n -> {
					try {
						readerSemaphore.acquire();
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
						Thread.currentThread().interrupt();
					}
					logger.info("Reader reading ... {}", n);
					writerSemaphore.release();
				});
			}
		}
		Semaphore writerSemaphore = new Semaphore(1);
		Semaphore readerSemaphore = new Semaphore(0);
		logger.info("writerSemaphore permit:1 | readerSemaphore permit:0");
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		executorService.submit(new Writer(readerSemaphore, writerSemaphore));
		executorService.submit(new Reader(readerSemaphore, writerSemaphore));
		executorService.shutdown();
	}

	/**
	 * Semaphore use even odd example.
	 */
	public void semaphoreUseEvenOddExample() {
		final int maxLimit = 30;
		class EvenFeed implements Runnable {
			int value;
			Semaphore evenSemaphore;
			Semaphore oddSemaphore;

			public EvenFeed(Semaphore oddSemaphore, Semaphore evenSemaphore, int value) {
				this.oddSemaphore = oddSemaphore;
				this.evenSemaphore = evenSemaphore;
				this.value = value;
			}

			public void run() {
				do {
					try {
						evenSemaphore.acquire();
						logger.info("EvenThread logging: {}", value);
						value = value + 2;
						oddSemaphore.release();
					} catch (InterruptedException e) {
						logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
						Thread.currentThread().interrupt();
					}
				} while (value <= maxLimit);
			}
		}

		class OddFeed implements Runnable {
			int value;
			Semaphore evenSemaphore;
			Semaphore oddSemaphore;

			public OddFeed(Semaphore oddSemaphore, Semaphore evenSemaphore, int value) {
				this.oddSemaphore = oddSemaphore;
				this.evenSemaphore = evenSemaphore;
				this.value = value;
			}

			public void run() {
				do {
					try {
						oddSemaphore.acquire();
						logger.info("OddThread logging: {}", value);
						value = value + 2;
						evenSemaphore.release();
					} catch (InterruptedException e) {
						logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
						Thread.currentThread().interrupt();
					}

				} while (value <= maxLimit);
			}
		}

		Semaphore evenSemaphore = new Semaphore(1);
		Semaphore oddSemaphore = new Semaphore(0);
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		executorService.submit(new EvenFeed(oddSemaphore, evenSemaphore, 0));
		executorService.submit(new OddFeed(oddSemaphore, evenSemaphore, 1));
		executorService.shutdown();
	}

	/***
	 * Lock use not Synchronized Block Lock interface with
	 * methods(lock,lockInterruptibly,tryLock,unlock) -> ReentrantLock class
	 * ReadWriteLock with methods( readLock, writeLock) -> ReentrantReadWriteLock
	 * class
	 */
	public void lockExample() {
		Lock lock = new ReentrantLock();
		class Room {
			public void takeIt(int cutomerId) {
				lock.lock();
				try {
					logger.info("cutomer {} stay and enjoy servies ...", cutomerId);
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
					Thread.currentThread().interrupt();
				} finally {
					logger.info("checkout customer {} from room..", cutomerId);
					lock.unlock();
				}
			}
		}
		class Booking implements Runnable {
			Room room;
			int customerId;

			public Booking(Room room, int customerId) {
				this.room = room;
				this.customerId = customerId;
			}

			public void run() {
				room.takeIt(customerId);
			}
		}
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		List<Pair<Integer, Future<String>>> list = new ArrayList<>();
		Stream.iterate(101, n -> n + 1).limit(20)
				.forEach(n -> list.add(Pair.of(n, executorService.submit(new Booking(new Room(), n), "COMPLETE"))));
		list.stream().forEach(n -> {
			try {
				logger.info("Customer:{} SERVICE: {} Notified", n.getKey(), n.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		});
		executorService.shutdown();
	}

	public void readWriteLockExample() {
		LinkedList<String> fileLines = new LinkedList<>();
		/***
		 * ReadLock : If no threads are writing then -> any number of threads acquire
		 * for read lock WriteLock : If no threads are reading and writing then -> a
		 * thread acquire for write lock
		 */
		FileActionReadWrite fileAction = new FileActionReadWrite(new ReentrantReadWriteLock(), fileLines);
		List<Pair<String, Future<String>>> list = new ArrayList<>();

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int limitVal = 10;
		Stream.iterate(0, n -> n + 1).limit(limitVal).forEach(n -> list.add(Pair.of(String.format(WRITE_INT, n),
				executorService.submit(() -> fileAction.write(String.format("I am not fine %d", n)), "COMPLETE"))));

		list.add(Pair.of(String.format(REMOVE_INT, limitVal), executorService.submit(fileAction::remove, "COMPLETE")));

		list.add(Pair.of(String.format(WRITE_INT, limitVal + 1), executorService
				.submit(() -> fileAction.write(String.format("I am fine %d", limitVal + 1)), "COMPLETE")));
		Future<String> readLastLine = executorService.submit(fileAction::read);
		Future<Boolean> isIAmFine = executorService.submit(() -> fileAction.contains("I am fine"));

		list.stream().forEach(n -> {
			try {
				logger.info("{} Value {}", n.getKey(), n.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		});
		try {
			logger.info(READ_VALUE_LOG, readLastLine.get());
			logger.info("Remove Value {}", isIAmFine.get());
		} catch (InterruptedException | ExecutionException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		logger.info("fileLines now {}", fileLines);

		executorService.shutdown();
	}

	/**
	 * Stamped lock example.
	 */
	public void stampedLockExample() {

		LinkedList<String> fileLines = new LinkedList<>();
		/***
		 * ReadLock : If no threads are writing then -> any number of threads acquire
		 * for read lock WriteLock : If no threads are reading and writing then -> a
		 * thread acquire for write lock
		 */
		FileActionStamped fileAction = new FileActionStamped(new StampedLock(), fileLines);
		List<Pair<String, Future<String>>> list = new ArrayList<>();

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int limitVal = 10;
		Stream.iterate(0, n -> n + 1).limit(limitVal)
				// writeLock
				.forEach(n -> list.add(Pair.of(String.format(WRITE_INT, n), executorService
						.submit(() -> fileAction.write(String.format("I am not fine %d", n)), "COMPLETE"))));
		// tryOptimisticRead :- read task.. Allow only when writer not locked
		Future<String> readOffWriterOn = executorService.submit(() -> fileAction.readOffWhenWriterOn().orElse("null"));
		// tryOptimisticRead :- contains(read) check when Write not locked
		Future<Optional<Boolean>> containsOffWhenWriterOn = executorService
				.submit(() -> fileAction.containsOffWhenWriterOn("I am not fine 1"));
		// writeLock
		list.add(Pair.of(String.format(REMOVE_INT, limitVal), executorService.submit(fileAction::remove, "COMPLETE")));
		// writeLock
		list.add(Pair.of(String.format(WRITE_INT, limitVal + 1), executorService
				.submit(() -> fileAction.write(String.format("I am fine %d", limitVal + 1)), "COMPLETE")));
		// readLock
		Future<String> readLastLine = executorService.submit(fileAction::read);
		// readLock
		Future<Boolean> isIAmFine = executorService.submit(() -> fileAction.contains("I am not fine 1"));
		/***
		 * In real time we can think above call and there return trigger from different
		 * services.. So result will not goes sequential like below
		 */
		list.stream().forEach(n -> {
			try {
				logger.info("{} Value {}", n.getKey(), n.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		});
		try {
			logger.info(READ_VALUE_LOG, readLastLine.get());
			logger.info("contains Value {}", isIAmFine.get());
			logger.info("readOffWriterOn {}", readOffWriterOn.get());
			logger.info("containsOffWhenWriterOn {}", containsOffWhenWriterOn.get(2, TimeUnit.SECONDS));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}
		logger.info("fileLines now {}", fileLines);

		executorService.shutdown();
	}

	public void customerServiceStampedExample() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Customer customer = new Customer();
		customer.setAccountNumber("323-333-343-0");
		customer.setName("Customer-0");
		customer.setWithdrawBalance(34000.00);
		BankingBalance bankingBalance = new BankingBalance(customer);
		executorService.submit(()->Stream.iterate(0,n->n+1).limit(100).forEach(n->bankingBalance.atmUpdate(PAYMENT_TYPE.DEBIT, 300.00)));
		logger.info("proceed");
		TimeUnit.SECONDS.sleep(1);
		bankingBalance.atmUpdate(PAYMENT_TYPE.STATUS, null);
	}

	private static void addCustomers() {
		for (int i = 1; i <100; i++) {
			Customer customer = new Customer();
			customer.setAccountNumber("323-333-343-"+i);
			customer.setName("Customer-"+i);
			customer.setWithdrawBalance(34000.00+i*10);
		}
	}

	/***
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ThreadingPractice tp = new ThreadingPractice();
		tp.customerServiceStampedExample();
	}
}
