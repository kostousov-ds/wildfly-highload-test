package ru.bisys.acquiring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import net.kst_d.common.HttpInteractionException;
import net.kst_d.common.NetUtils;
import net.kst_d.common.SessionIDGenerator;
import net.kst_d.common.TicSessionID;
import net.kst_d.common.Try;
import net.kst_d.common.Two;
import net.kst_d.common.log.Logger;
import net.kst_d.common.log.LoggerFactory;
import net.kst_d.common.log.MethodLogger;

import static org.testng.Assert.fail;

public class HighloadIT {
    public static final int TASK_COUNT = 50;
    public static final int POOL_SIZE = 1;
    private static final Logger LOG = LoggerFactory.getLogger(HighloadIT.class);
    public static final String BASE_URL = "http://localhost:8080/wh-test/api/one";



    @Test
    public void massivePays() {
	final TicSessionID ticSessionID = new TicSessionID(SessionIDGenerator.getNewSessionID());
	final MethodLogger logger = LOG.entering(ticSessionID, "massivePays");

	try {
	    ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);

	    Collection<Callable<Boolean>> tasks = new LinkedList<>();
	    for (int i = 0; i < TASK_COUNT; i++) {
		tasks.add(() -> {
		    final String sid = SessionIDGenerator.getNewSessionID();
		    final TicSessionID ticSessionID1 = new TicSessionID(sid);
		    final List<Two<String, String>> params = new ArrayList<>(10);
		   	params.addAll(
		   	    Arrays.asList(
		   		p("sid", sid),
		   		p("CURRENCY", "RUR"),
		   		p("TRTYPE", "1"),
		   		p("COUNTRY", "RU")
		   	    )
		   	);

		   	final long timeBefore = System.currentTimeMillis();
		    String reply= null;
		    try {
			    reply = NetUtils.post(200, BASE_URL, ticSessionID1, params);
			    logger.trace("reply {}", reply);
			} catch (HttpInteractionException e) {
		   	    logger.error("", e);
		   	}
		   	final long timeAfter = System.currentTimeMillis();
		   	logger.trace("reply {}, time: {}", reply, timeAfter - timeBefore);
		    return sid.equals(reply);
		});
	    }
	    logger.trace("all tasks was added");

	    long startMillis = System.currentTimeMillis();
	    final List<Future<Boolean>> results = service.invokeAll(tasks);
	    service.shutdown();
	    while (!service.isTerminated()) {
		Thread.sleep(50);
	    }
	    long stopMillis = System.currentTimeMillis();
	    long time = (stopMillis - startMillis);
	    final Set<Try<Boolean>> done = results.stream().filter(Future::isDone).<Try<Boolean>>map(r -> Try.of(r::get)).filter(Try::isSuccess).collect(Collectors.toSet());
	    logger.trace("stopped tasks {}", done.size());

	    final long success = done.stream().filter(Try::get).count();
	    final long fail = done.stream().filter(t -> !t.get()).count();
	    logger.info("tasks {}, success {}, fail {}, time {}, speed {}", TASK_COUNT, success, fail, time, TASK_COUNT * 1000 / time);
	    logger.info("poll size {}", POOL_SIZE);
	    if (success != TASK_COUNT) {
		fail("expected " + TASK_COUNT + " success tasks, received " + success);
	    }
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
