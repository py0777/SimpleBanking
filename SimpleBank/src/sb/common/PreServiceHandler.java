package sb.common;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import nexcore.framework.core.data.CaseIgnoreHashMap;
import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;

import org.apache.log4j.Logger;

import sb.service.om.OmAcnoDacaInqr;
import sb.service.om.OmAcnoGen;
import sb.service.om.OmDacaRctmPrcs;

@Path("helloworld")
public class PreServiceHandler {
	
	static Logger logger = Logger.getLogger(PreServiceHandler.class);
	public IDataSet preServiceHandler(IDataSet requestData) throws Exception{
		
		
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		
		OmAcnoGen omAcnoGen = new OmAcnoGen();
		OmAcnoDacaInqr omAcnoDacaInqr = new OmAcnoDacaInqr();
		OmDacaRctmPrcs omDacaRctmPrcs = new OmDacaRctmPrcs();
		try 
		{
			if("OmAcnoGen".equals(requestData.getField("SERVICE"))) {
				logger.debug("########### OmAcnoGen START #########");
				responseData= omAcnoGen.omAcnoGen(requestData);
				
			}
			else if("OmAcnoDacaInqr".equals(requestData.getField("SERVICE"))) {
				responseData= omAcnoDacaInqr.omAcnoDacaInqr(requestData);
			}
			else if("OmDacaRctmPrcs".equals(requestData.getField("SERVICE"))) {
				responseData= omDacaRctmPrcs.omDacaRctmPrcs(requestData);
			}
			
			
			dh.getSession().commit();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			dh.getSession().rollback();
			/*에러는 무시하고 롤백만 함.*/	
		} finally {
			dh.closeSession();
		}
		
		return responseData;
	}
	
//	 @GET
//	 @Produces("text/plain")
//	 /*application/json*/
//	 public String getMessage() {
//	      return "Rest Never Sleeps";
//	 }
	 
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 /*application/json text/plain  */
	 public String getMessage(@QueryParam("SERVICE_NAME")String serviceName,@QueryParam("ACNO")String acno,@QueryParam("TOT_TR_AMT")long totAmt) {
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		IDataSet requestData = new DataSet();
		CaseIgnoreHashMap hsm  = null;
		
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		
		OmAcnoGen omAcnoGen = new OmAcnoGen();
		OmDacaRctmPrcs omDacaRctmPrcs = new OmDacaRctmPrcs();
		try 
		{
			 if("OmAcnoGen".equals(serviceName)) {
					logger.debug("########### OmAcnoGen START #########");
					requestData.putField("ACNO", acno);
					responseData= omAcnoGen.omAcnoGen(requestData);
					logger.debug("responseData.getFieldMap()"+responseData.getRecordSet("ResultSet"));
					
			 }
			 else if("OmDacaRctmPrcs".equals(serviceName)) {
					logger.debug("########### OmAcnoGen START #########");
					requestData.putField("ACNO", acno);
					requestData.putField("TOT_TR_AMT", totAmt);
					responseData= omDacaRctmPrcs.omDacaRctmPrcs(requestData);
					logger.debug("responseData.getFieldMap()"+responseData.getRecordSet("ResultSet"));
					
			 }
			dh.getSession().commit();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			dh.getSession().rollback();
			/*에러는 무시하고 롤백만 함.*/	
		} finally {
			dh.closeSession();
		}
		 
	      return acno;
	 }
}
