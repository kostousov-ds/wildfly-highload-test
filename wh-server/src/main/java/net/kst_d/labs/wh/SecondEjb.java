package net.kst_d.labs.wh;

import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import net.kst_d.common.SessionIDGenerator;
import net.kst_d.common.TicSessionID;
import net.kst_d.common.log.Logger;
import net.kst_d.common.log.LoggerFactory;
import net.kst_d.common.log.MethodLogger;

@Stateless
@Path ("/two")
public class SecondEjb {
    private static final Logger LOG = LoggerFactory.getLogger(SecondEjb.class);

    @POST
    @Path("/")
    public String doSomething(@FormParam ("sid") String sid) {
	final TicSessionID ticSessionID = new TicSessionID(sid == null ? SessionIDGenerator.getNewSessionID() : sid);
	final MethodLogger logger = LOG.entering(ticSessionID, "doSomething");


	String ret = "Something";


	logger.exiting(ret);
	return ret;
    }


}
