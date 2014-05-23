package net.kst_d.labs.wh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import net.kst_d.common.HttpInteractionException;
import net.kst_d.common.NetUtils;
import net.kst_d.common.SessionIDGenerator;
import net.kst_d.common.TicSessionID;
import net.kst_d.common.Two;
import net.kst_d.common.log.Logger;
import net.kst_d.common.log.LoggerFactory;
import net.kst_d.common.log.MethodLogger;

@Path ("/one")
@Stateless
public class FirstEjb {
    private static final Logger LOG = LoggerFactory.getLogger(FirstEjb.class);

    public static final String URL = "http://localhost:8080/wh-test/two";

    @POST
    @Path ("/")
    public String doSomething(@FormParam ("sid") String sid) {
	final TicSessionID ticSessionID = new TicSessionID(sid == null ? SessionIDGenerator.getNewSessionID() : sid);
	final MethodLogger logger = LOG.entering(ticSessionID, "doSomething");


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
	String ret;
	try {
	    ret = NetUtils.post(200, URL, ticSessionID, params);
	} catch (HttpInteractionException e) {
	    logger.error("", e);
	    ret = "error";
	}
	final long timeAfter = System.currentTimeMillis();
	logger.trace("request time: {}", timeAfter - timeBefore);

	logger.exiting(ret);
	return ret;
    }

    private static Two<String, String> p(String a, String b) {
	return new Two<>(a, b);
    }


}
