package sb.service.om;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.service.cm.CmAcnoDacaInqr;
import sb.service.cm.CmDacaAltnDrwgPrcs;
import sb.service.cm.CmTrnoGen;

public class OmDacaAltnDrwgPrcs
{
static Logger logger = Logger.getLogger(OmDacaRctmPrcs.class);
	
	public IDataSet omDacaAltnDrwgPrcs(IDataSet requestData)throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
	
		/*CM출금거래번호생성*/
		CmTrnoGen cmDrwgTrnoGen = new CmTrnoGen();
		/*CM입금거래번호생성*/
		CmTrnoGen cmRctmTrnoGen = new CmTrnoGen();
		/*CM출금계좌예수금조회*/
		CmAcnoDacaInqr cmDrwgAcnoDacaInqr = new CmAcnoDacaInqr();
		/*CM출금계좌예수금조회*/
		CmAcnoDacaInqr cmRctmAcnoDacaInqr = new CmAcnoDacaInqr();
		/*CM예수금대체출금처리*/
		CmDacaAltnDrwgPrcs cmDacaAltnDrwgPrcs = new CmDacaAltnDrwgPrcs();
		
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
			 *  출금 계좌잔고정보조회
			 ********************************************************************/
			IDataSet cmDrwgAcnoDacaInqrDsIn = new DataSet();
			IDataSet cmDrwgAcnoDacaInqrDsOut = new DataSet();
			
			cmDrwgAcnoDacaInqrDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			
			cmDrwgAcnoDacaInqrDsOut = cmDrwgAcnoDacaInqr.cmAcnoDacaInqr(cmDrwgAcnoDacaInqrDsIn);
			
			logger.debug("출금계좌예수금: " + cmDrwgAcnoDacaInqrDsOut.getLongField("DACA") );
			
			/********************************************************************
			 *  입금 계좌잔고정보조회
			 ********************************************************************/
			IDataSet cmRctmAcnoDacaInqrDsIn = new DataSet();
			IDataSet cmRcmtAcnoDacaInqrDsOut = new DataSet();
			
			cmRctmAcnoDacaInqrDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			
			cmRcmtAcnoDacaInqrDsOut = cmRctmAcnoDacaInqr.cmAcnoDacaInqr(cmRctmAcnoDacaInqrDsIn);
			
			logger.debug("입금계좌예수금: " + cmRcmtAcnoDacaInqrDsOut.getLongField("DACA") );
			
			/********************************************************************
			 *  출금거래일련번호 채번
			 ********************************************************************/
			IDataSet cmDrwgTrnoGenDsIn = new DataSet();
			IDataSet cmDrwgTrnoGenDsOut = new DataSet();
			
			cmDrwgTrnoGenDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			cmDrwgTrnoGenDsIn.putField("TR_DT", today);
			
			cmDrwgTrnoGenDsOut = cmDrwgTrnoGen.cmTrnoGen(cmDrwgTrnoGenDsIn);
			
			logger.debug("출금거래번호: " + cmDrwgTrnoGenDsOut.getLongField("TR_NO") );
			
			/********************************************************************
			 *  입금거래일련번호 채번
			 ********************************************************************/
			IDataSet cmRcmtTrnoGenDsIn = new DataSet();
			IDataSet cmRcmtTrnoGenDsOut = new DataSet();
			
			cmRcmtTrnoGenDsIn.putField("ACNO", requestData.getField("RCTM_ACNO"));
			cmRcmtTrnoGenDsIn.putField("TR_DT", today);
			
			cmRcmtTrnoGenDsOut = cmRctmTrnoGen.cmTrnoGen(cmRcmtTrnoGenDsIn);
			
			logger.debug("입금거래번호: " + cmRcmtTrnoGenDsOut.getLongField("TR_NO") );
			
			/********************************************************************
			 *  대체처리
			 ********************************************************************/
			IDataSet cmDacaAltnDrwgPrcsDSIn = new DataSet();
			
			cmDacaAltnDrwgPrcsDSIn.putField("TR_DT", today);
			cmDacaAltnDrwgPrcsDSIn.putField("DRWG_ACNO", requestData.getField("DRWG_ACNO"));
			cmDacaAltnDrwgPrcsDSIn.putField("ATLN_AMT", requestData.getField("TOT_TR_AMT"));
			cmDacaAltnDrwgPrcsDSIn.putField("DRWG_TR_NO", cmDrwgTrnoGenDsOut.getLongField("TR_NO"));
			cmDacaAltnDrwgPrcsDSIn.putField("DRWG_STRT_TR_NO", cmDrwgTrnoGenDsOut.getLongField("TR_NO"));
			cmDacaAltnDrwgPrcsDSIn.putField("DRWG_SYNS_CD", "004");  /*대체출금*/
			cmDacaAltnDrwgPrcsDSIn.putField("RCTM_ACNO", requestData.getField("RCTM_ACNO"));
			cmDacaAltnDrwgPrcsDSIn.putField("RCTM_TR_NO", cmRcmtTrnoGenDsOut.getLongField("TR_NO"));
			cmDacaAltnDrwgPrcsDSIn.putField("RCTM_TR_NO", cmRcmtTrnoGenDsOut.getLongField("TR_NO"));
			cmDacaAltnDrwgPrcsDSIn.putField("RCTM_SYNS_CD", "003");  /*대체입금*/
			
			
			responseData = cmDacaAltnDrwgPrcs.cmDacaAltnDrwgPrcs(cmDacaAltnDrwgPrcsDSIn);
			
			logger.debug("responseData: " + responseData);
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
		if( !StringUtils.isEmpty(requestData.getField("DRWG_ACNO"))
				&& StringUtils.length(requestData.getField("DRWG_ACNO")) != 11) {
			logger.error("출금계좌번호를 확인하세요.");
			throw new Exception("출금계좌번호를 확인하세요.");
		}
		
		/*계좌번호체크*/
		if( !StringUtils.isEmpty(requestData.getField("RCTM_ACNO"))
				&& StringUtils.length(requestData.getField("RCTM_ACNO")) != 11) {
			logger.error("입금계좌번호를 확인하세요.");
			throw new Exception("입금계좌번호를 확인하세요.");
		}
		
		/*총거래금액체크*/
		if( requestData.getLongField("TOT_TR_AMT") <= 0) {
			throw new Exception("총거래금액 확인하세요.");
		}
	}
		
}
