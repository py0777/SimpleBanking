package sb.service.cm;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.common.DaoHandler;

public class TrDetlRfct {

	static Logger logger = Logger.getLogger(AcnoGen.class);
	private final String rpd1000Dft = "sb.repository.mapper.RPD1000_DefaultMapper";
	private final String rpb1000Dft = "sb.repository.mapper.RPB1000_DefaultMapper";
	private final String rpa0100Dft = "sb.repository.mapper.RPA0100_DefaultMapper";
	public IDataSet asTrDetlRfct(IDataSet requestData) throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		IDataSet dsRPA0100Q01= null;
		IDataSet dsRPB1000Q01 = null;
		IDataSet dsRPB1000I01 = new DataSet();
				
		int    rsCnt = 0;  /*결과 건수*/

		try
		{
			/*입력값 체크*/
			initCheck(requestData);
			
			/***********************************************************************
		    * 적요조회 호출
		    ***********************************************************************/
			dsRPA0100Q01 = dh.selectOneSql(requestData, rpa0100Dft+"."+"S001");
			
			logger.debug("적요조회 : " + dsRPA0100Q01);
		    /***********************************************************************
		    * 계좌예수금기본조회 호출
		    ***********************************************************************/
			dsRPB1000Q01 = dh.selectOneSql(requestData, rpb1000Dft+"."+"S001");
			
			logger.debug("계좌예수금조회 : " + dsRPB1000Q01);
			
		    /***********************************************************************
		    * 거래내역 생성
		    ***********************************************************************/
			logger.debug("[START]거래내역 생성  ");
			
			dsRPB1000I01.putField("TR_DT", requestData.getField("TR_DT"));
			dsRPB1000I01.putField("ACNO", requestData.getField("ACNO"));
			dsRPB1000I01.putField("TR_NO", requestData.getLongField("TR_SN"));
			dsRPB1000I01.putField("SYNS_CD", requestData.getField("SYNS_CD"));
			dsRPB1000I01.putField("TR_TP_DCD", dsRPA0100Q01.getField("TR_TP_DCD"));
			dsRPB1000I01.putField("TR_AMT", requestData.getLongField("TR_AMT"));
			dsRPB1000I01.putField("BF_DACA", requestData.getLongField("BF_DACA"));
			dsRPB1000I01.putField("AF_DACA", requestData.getLongField("AF_DACA"));
			dsRPB1000I01.putField("CNCL_YN", "N");
			dsRPB1000I01.putField("STRT_TR_NO", requestData.getLongField("STRT_TR_NO"));
			dsRPB1000I01.putField("ORGN_TR_NO", requestData.getLongField("ORGN_TR_NO"));
			dsRPB1000I01.putField("CLNT_NM", requestData.getField("CLNT_NM"));
			
			logger.debug("거래내역 생성INPUT " + dsRPB1000I01);
			
			rsCnt = dh.insertSql(dsRPB1000I01, rpd1000Dft+"."+"I001");
			
			if(rsCnt <=  0) {
				throw new Exception( rpb1000Dft+"."+"U001"+" 등록오류.");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
		/*************************************************************
		 * Retrun Result Data
		 *************************************************************/
		
		responseData.setOkResultMessage("OK", new String[]{"처리완료되었습니다."});
		
		return responseData;
	}
	/*initCheck*/
	private void initCheck(IDataSet requestData) throws Exception {		
		
			/*일자체크*/
			if( StringUtils.isEmpty(requestData.getField("TR_DT"))
					||StringUtils.length(requestData.getField("TR_DT")) != 8) {
				throw new Exception("거래일자를 확인하세요.");
			}
			
			/*계좌번호체크*/
			if( StringUtils.isEmpty(requestData.getField("ACNO"))
					|| StringUtils.length(requestData.getField("ACNO")) != 10) {
				throw new Exception("계좌번호를 확인하세요.");
			}
			
			/*거래번호체크*/
			if( requestData.getLongField("TR_SN") <= 0) {
				throw new Exception("거래번호 확인하세요.");
			}
	}
}