package sb.common;

import org.apache.log4j.Logger;

import nexcore.framework.core.data.IDataSet;

import sb.service.om.OmAcnoDacaInqr;
import sb.service.om.OmAcnoGen;
import sb.service.om.OmDacaRctmPrcs;

public class PreServiceHandler {
	
	static Logger logger = Logger.getLogger(DaoHandler.class);
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
				responseData= omAcnoGen.omAcnoGen(requestData);
				
			}
			else if("OmAcnoDacaInqr".equals(requestData.getField("SERVICE"))) {
				
			}
			else if("OmDacaRctmPrcs".equals(requestData.getField("SERVICE"))) {
				
			}
			
			
			dh.getSession().commit();
			return responseData;
		}catch (Exception e) {
			
			e.printStackTrace();
			dh.getSession().rollback();
				
		} finally {
			dh.closeSession();
		}
	}
}
