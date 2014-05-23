package ru.bisys.acquiring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import net.kst_d.common.HttpInteractionException;
import net.kst_d.common.NetUtils;
import net.kst_d.common.SessionIDGenerator;
import net.kst_d.common.TicSessionID;
import net.kst_d.common.Two;
import net.kst_d.common.log.Logger;
import net.kst_d.common.log.LoggerFactory;
import net.kst_d.common.log.MethodLogger;

import static org.testng.Assert.fail;

public class HighloadIT {
    public static final int TASK_COUNT = 20;
    public static final int POOL_SIZE = 16;
    private static final Logger LOG = LoggerFactory.getLogger(HighloadIT.class);
    public static final String BASE_URL = "http://localhost:8080/wh-test/one";



    @Test
    public void massivePays() {
	final TicSessionID ticSessionID = new TicSessionID(SessionIDGenerator.getNewSessionID());
	final MethodLogger logger = LOG.entering(ticSessionID, "massivePays");

	try {
	    ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);

	    Collection<Callable<Void>> tasks = new LinkedList<>();
	    for (int i = 0; i < TASK_COUNT; i++) {
		tasks.add(() -> {
		    final TicSessionID ticSessionID1 = new TicSessionID(SessionIDGenerator.getNewSessionID());
		    final List<Two<String, String>> params = new ArrayList<>(10);
		   	params.addAll(
		   	    Arrays.asList(
		   		p("sid", ticSessionID1.getSessionID()),
		   		p("CURRENCY", "RUR"),
		   		p("TRTYPE", "1"),
		   		p("COUNTRY", "RU")
		   	    )
		   	);

		   	final long timeBefore = System.currentTimeMillis();
		   	try {
			    String reply = NetUtils.post(200, BASE_URL, ticSessionID, params);
			    logger.trace("reply {}", reply);
			} catch (HttpInteractionException e) {
		   	    logger.error("", e);
		   	}
		   	final long timeAfter = System.currentTimeMillis();
		   	logger.trace("request time: {}", timeAfter - timeBefore);
		    return null;
		});
	    }
	    logger.trace("all tasks was added");

	    long startMillis = System.currentTimeMillis();
	    service.invokeAll(tasks);
	    service.shutdown();
	    while (!service.isTerminated()) {
		Thread.sleep(50);
	    }
	    long stopMillis = System.currentTimeMillis();
	    long time = (stopMillis - startMillis);
	    logger.info("tasks {}, time {}, speed {}", TASK_COUNT, time, TASK_COUNT * 1000 / time);
	    logger.info("poll size {}", POOL_SIZE);
	} catch (Exception e) {
	    logger.error("", e);
	    fail(e.getMessage());
	}

	logger.exiting();
    }

    private static Two<String, String> p(String a, String b) {
	return new Two<>(a, b);
    }

}
