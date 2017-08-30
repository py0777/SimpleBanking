package sb.service.om;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.service.cm.CmAcnoDacaInqr;
import sb.service.cm.CmAcnoGen;
import sb.service.cm.CmDacaRctmPrcs;
import sb.service.cm.CmTrnoGen;

public class OmDacaRctmPrcs
{
	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	
	public IDataSet omDacaRctmPrcs(IDataSet requestData)throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		
		
		/*CM예수금입금처리*/
		CmDacaRctmPrcs cmDacaRctmPrcs = new CmDacaRctmPrcs();
		/*CM거래번호생성*/
		CmTrnoGen cmTrnoGen = new CmTrnoGen();
		/*CM계좌예수금조회*/
		CmAcnoDacaInqr cmAcnoDacaInqr = new CmAcnoDacaInqr();
		
		/*일자설정*/
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		
		try
		{
			Date d = gc.getTime();
			today = sdformat.format(d);
			requestData.putField("TR_DT", today);
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
			/********************************************************************
			 *  계좌잔고정보조회
			 ********************************************************************/
			IDataSet cmAcnoDacaInqrDsIn = new DataSet();
			IDataSet cmAcnoDacaInqrDsOut = new DataSet();
			
			cmAcnoDacaInqrDsIn.putField("ACNO", requestData.getField("ACNO"));
			
			cmAcnoDacaInqrDsOut = cmAcnoDacaInqr.cmAcnoDacaInqr(cmAcnoDacaInqrDsIn);
			
			logger.debug("계좌예수금: " + cmAcnoDacaInqrDsOut.getLongField("DACA") );
			/********************************************************************
			 *  거래일련번호 채번
			 ********************************************************************/
			IDataSet cmTrnoGenDsIn = new DataSet();
			IDataSet cmTrnoGenDsOut = new DataSet();
			
			cmTrnoGenDsIn.putField("ACNO", requestData.getField("ACNO"));
			cmTrnoGenDsIn.putField("TR_DT", today);
			
			cmTrnoGenDsOut = cmTrnoGen.cmTrnoGen(cmTrnoGenDsIn);
			
			logger.debug("거래번호: " + cmTrnoGenDsOut.getLongField("TR_NO") );
			/********************************************************************
			 *  입금처리
			 ********************************************************************/
			IDataSet cmDacaRctmPrcsDSIn = new DataSet();
			
			cmDacaRctmPrcsDSIn.putField("TR_DT", requestData.getField("TR_DT") );			
			cmDacaRctmPrcsDSIn.putField("ACNO", requestData.getField("ACNO") );
			cmDacaRctmPrcsDSIn.putField("TR_NO", cmTrnoGenDsOut.getLongField("TR_NO") );
			cmDacaRctmPrcsDSIn.putField("STRT_TR_SN", cmTrnoGenDsOut.getLongField("TR_NO") );
			cmDacaRctmPrcsDSIn.putField("TOT_TR_AMT", requestData.getLongField("TOT_TR_AMT"));
			cmDacaRctmPrcsDSIn.putField("SYNS_CD", "001");  /* 적요코드 001: 입금*/
			
			responseData = cmDacaRctmPrcs.cmDacaRctmPrcs(cmDacaRctmPrcsDSIn);
			
			
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
		logger.debug("[Start] initCheck");
		 
		/*계좌번호체크*/
		if( !StringUtils.isEmpty(requestData.getField("ACNO"))
				&& StringUtils.length(requestData.getField("ACNO")) != 11) {
			logger.error("계좌번호를 확인하세요.");
			throw new Exception("계좌번호를 확인하세요.");
		}
		
		/*총거래금액체크*/
		if( requestData.getLongField("TOT_TR_AMT") <= 0) {
			throw new Exception("총거래금액 확인하세요.");
		}
		
		
	}
}
