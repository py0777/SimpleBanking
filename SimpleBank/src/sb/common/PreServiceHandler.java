package sb.common;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import sb.service.om.OmAcnoDacaInqr;
import sb.service.om.OmAcnoGen;
import sb.service.om.OmDacaAltnDrwgPrcs;
import sb.service.om.OmDacaRctmPrcs;

@Path("restapi")
public class PreServiceHandler {
	
	static Logger logger = Logger.getLogger(PreServiceHandler.class);
	public void preServiceHandler(IDataSet requestData) throws Exception{
		
		
//		/*************************************************************
//		 * Declare Var
//		 *************************************************************/
//		IDataSet responseData = new DataSet();
//		
//		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
//		
//		OmAcnoGen omAcnoGen = new OmAcnoGen();
//		OmAcnoDacaInqr omAcnoDacaInqr = new OmAcnoDacaInqr();
//		OmDacaRctmPrcs omDacaRctmPrcs = new OmDacaRctmPrcs();
//		try 
//		{
//			if("OmAcnoGen".equals(requestData.getField("SERVICE"))) {
//				logger.debug("########### OmAcnoGen START #########");
//				responseData= omAcnoGen.omAcnoGen(requestData);
//				
//			}
//			else if("OmAcnoDacaInqr".equals(requestData.getField("SERVICE"))) {
//				responseData= omAcnoDacaInqr.omAcnoDacaInqr(requestData);
//			}
//			else if("OmDacaRctmPrcs".equals(requestData.getField("SERVICE"))) {
//				responseData= omDacaRctmPrcs.omDacaRctmPrcs(requestData);
//			}
//			
//			
//			dh.getSession().commit();
//			
//		}catch (Exception e) {
//			
//			e.printStackTrace();
//			dh.getSession().rollback();
//			/*에러는 무시하고 롤백만 함.*/	
//		} finally {
//			dh.closeSession();
//		}
		
	}
	
//	 @GET
//	 @Produces("text/plain")
//	 /*application/json*/
//	 public String getMessage() {
//	      return "Rest Never Sleeps";
//	 }
	 
	 @GET
	 @Produces(MediaType.APPLICATION_JSON+ ";charset=UTF-8")
	 /*application/json text/plain  */
	 public String getMessage(@QueryParam("SERVICE_NAME")String serviceName,@QueryParam("ID")String acno,@QueryParam("amount")long totAmt,@QueryParam("ID_send")String drwgAcno,@QueryParam("ID_rcv")String rctmAcno) throws Exception {
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		IDataSet requestData = new DataSet();
		JSONObject json = new JSONObject();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		
		OmAcnoGen omAcnoGen = new OmAcnoGen();
		OmDacaRctmPrcs omDacaRctmPrcs = new OmDacaRctmPrcs();
		OmDacaAltnDrwgPrcs omDacaAltnDrwgPrcs = new OmDacaAltnDrwgPrcs();
		OmAcnoDacaInqr omAcnoDacaInqr = new OmAcnoDacaInqr();
		
		try 
		{
			 if("IDGen".equals(serviceName)) {
					logger.debug("########### OmAcnoGen START #########");
					requestData.putField("ACNO", acno);
					responseData= omAcnoGen.omAcnoGen(requestData);
					logger.debug("responseData.getFieldMap()"+responseData.getRecordSet("ResultSet"));
					
			 }
			 else if("Rctm".equals(serviceName)) {
					logger.debug("########### OmDacaRctmPrcs START #########");
					requestData.putField("ACNO", acno);
					requestData.putField("TOT_TR_AMT", totAmt);
					responseData= omDacaRctmPrcs.omDacaRctmPrcs(requestData);
					logger.debug("responseData.getFieldMap()"+responseData.getFieldMap());
			 }
			 else if("Altn".equals(serviceName)) {
					logger.debug("########### OmDacaAltnDrwgPrcs START #########");
					requestData.putField("DRWG_ACNO", drwgAcno);
					requestData.putField("RCTM_ACNO", rctmAcno);
					requestData.putField("TOT_TR_AMT", totAmt);
					responseData= omDacaAltnDrwgPrcs.omDacaAltnDrwgPrcs(requestData);
					logger.debug("responseData.getFieldMap()"+responseData.getFieldMap());
					
			 }
			 else if("DacaInqr".equals(serviceName)) {
					logger.debug("########### OmAcnoDacaInqr START #########");
					requestData.putField("ACNO", acno);
					responseData= omAcnoDacaInqr.omAcnoDacaInqr(requestData);
			 }
			 
			 
			for( Map.Entry<String, Object> entry : responseData.getFieldMap().entrySet() ) {	
				String key = entry.getKey();
				Object value = entry.getValue();
				json.put(key, value);
			}
			json.put("retrun_code", 0);
			dh.getSession().commit();
			
		}catch (Exception e) {
			json.put("retrun_code", -1);
			json.put("retrun_msg", e.getMessage());
			e.printStackTrace();
			dh.getSession().rollback();
			/*에러는 무시하고 롤백만 함.*/	
		} finally {
			dh.closeSession();
		}
		 
	      return String.valueOf(json);
	 }
}
