package sb.service.cm;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.common.DaoHandler;

public class CmDacaAltnDrwgPrcs
{
	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	private final String rpb1000Dft = "sb.repository.mapper.RPB1000_DefaultMapper";
	public IDataSet cmDacaAltnDrwgPrcs(IDataSet requestData) throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		int    rsCnt = 0;  /*결과 건수*/
		
		
		try
		{
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
			/********************************************************************
			 *  당사대체처리
			 ********************************************************************/
			
			/***********************************************************************
		    * 출금계좌 예수금조회
		    ***********************************************************************/
			CmAcnoDacaInqr cmAcnoDacaInqr = new CmAcnoDacaInqr();
			IDataSet drwgAcnoDacaInqrDsIn = new DataSet();
			IDataSet drwgAcnoDacaInqrDsOut = null;
			
			drwgAcnoDacaInqrDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			
			drwgAcnoDacaInqrDsOut = cmAcnoDacaInqr.cmAcnoDacaInqr(drwgAcnoDacaInqrDsIn);
			
			/*출금가능금액조회*/
			/***********************************************************************
		    * 출금가능금액조회
		    ***********************************************************************/
			CmDrwgPsblAmtInqr cmDrwgPsblAmtInqr = new CmDrwgPsblAmtInqr();
			IDataSet cmDrwgPsblAmtInqrDsIn = new DataSet();
			IDataSet cmDrwgPsblAmtInqrDsOut = null;
			
			cmDrwgPsblAmtInqrDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			
			cmDrwgPsblAmtInqrDsOut = cmDrwgPsblAmtInqr.cmDrwgPsblAmtInqr(drwgAcnoDacaInqrDsIn);
			
			if(requestData.getLongField("ATLN_AMT") > cmDrwgPsblAmtInqrDsOut.getLongField("DRWG_PABL_AMT"))
			{
				logger.error("대체금액이 출금가능금액보다 큽니다.");
				throw new Exception("대체금액이 출금가능금액보다 큽니다.");
			}
			
			/***********************************************************************
		    * 출금반영 호출
		    ***********************************************************************/
			CmDacaDrwgRfct cmDacaDrwgRfct = new CmDacaDrwgRfct();
			IDataSet cmDacaDrwgRfctDsIn = new DataSet();
			IDataSet cmDacaDrwgRfctDsOut = null;
			
			cmDacaDrwgRfctDsIn.putField("TR_DT", requestData.getField("TR_DT"));
			cmDacaDrwgRfctDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			cmDacaDrwgRfctDsIn.putField("DRWG_AMT", requestData.getField("ATLN_AMT"));
			
			cmDacaDrwgRfctDsOut= cmDacaDrwgRfct.cmDacaDrwgRfct(cmDacaDrwgRfctDsIn);
			
			/***********************************************************************
		    * 거래내역반영 호출
		    ***********************************************************************/
			CmTrDetlRfct cmTrDetlRfct = new CmTrDetlRfct();
			IDataSet cmTrDetlRfctDsIn = new DataSet();
			IDataSet cmTrDetlRfctDsOut = null;
			
			
			cmTrDetlRfctDsIn.putField("TR_DT", requestData.getField("TR_DT"));
			cmTrDetlRfctDsIn.putField("ACNO", requestData.getField("DRWG_ACNO"));
			cmTrDetlRfctDsIn.putField("TR_NO", requestData.getLongField("DRWG_TR_SN"));
			cmTrDetlRfctDsIn.putField("SYNS_CD", requestData.getField("DRWG_SYNS_CD"));
			cmTrDetlRfctDsIn.putField("TR_AMT", requestData.getLongField("ATLN_AMT"));
			cmTrDetlRfctDsIn.putField("BF_DACA", cmDacaDrwgRfctDsOut.getLongField("BF_DACA"));
			cmTrDetlRfctDsIn.putField("AF_DACA", cmDacaDrwgRfctDsOut.getLongField("AF_DACA"));
			cmTrDetlRfctDsIn.putField("CNCL_YN", "N");
			cmTrDetlRfctDsIn.putField("STRT_TR_NO", requestData.getLongField("DRWG_STRT_TR_NO"));
			
			
			cmTrDetlRfctDsOut= cmTrDetlRfct.cmTrDetlRfct(cmTrDetlRfctDsIn);
			
			/***********************************************************************
		    * 연계거래내역반영
		    ***********************************************************************/
			logger.debug("[START]연계거래내역생성  ");
			IDataSet dsRPB1010I01 = new DataSet();
			String rpd1010Dft = "sb.repository.mapper.RPD1010_DefaultMapper";
			
			dsRPB1010I01.putField("TR_DT", requestData.getField("TR_DT"));
			dsRPB1010I01.putField("ACNO", requestData.getField("DRWG_ACNO"));
			dsRPB1010I01.putField("TR_NO", requestData.getLongField("DRWG_TR_SN"));
			dsRPB1010I01.putField("REL_TR_DT", requestData.getField("TR_DT"));
			dsRPB1010I01.putField("OPNT_ACNO", requestData.getField("RCTM_ACNO"));
			dsRPB1010I01.putField("OPNT_TR_NO", requestData.getLongField("RCTM_TR_SN"));
			//dsRPB1010I01.putField("REL_COM_ACTNO", requestData.getLongField("BF_DACA"));
			//dsRPB1010I01.putField("OPNT_ACT_NM", requestData.getLongField("AF_DACA"));
			//dsRPB1010I01.putField("REL_COM_CD", "N");
			
			logger.debug("연계거래내역 생성INPUT " + dsRPB1010I01);
			
			rsCnt = dh.insertSql(dsRPB1010I01, rpd1010Dft+"."+"I001");
			
			if(rsCnt <=  0) {
				throw new Exception( rpb1000Dft+"."+"I001"+" 등록오류.");
			}
			
			/*입금반영 호출*/
			CmDacaRctmRfct cmDacaRctmRfct = new CmDacaRctmRfct();
			IDataSet cmDacaRctmRfctDsIn = new DataSet();
			IDataSet cmDacaRctmRfctDsOut = null;
			
			cmDacaRctmRfctDsIn.putField("TR_DT", requestData.getField("TR_DT"));
			cmDacaRctmRfctDsIn.putField("ACNO", requestData.getField("RCTM_ACNO"));
			cmDacaRctmRfctDsIn.putField("DRWG_AMT", requestData.getField("ATLN_AMT"));
			
			cmDacaRctmRfctDsOut= cmDacaRctmRfct.cmDacaRctmRfct(cmDacaRctmRfctDsIn);
			
			/*거래내역반영 호출*/
			CmTrDetlRfct rctmTrDetlRfct = new CmTrDetlRfct();
			IDataSet rctmTrDetlRfctDsIn = new DataSet();
			IDataSet rctmTrDetlRfctDsOut = null;
			
			
			rctmTrDetlRfctDsIn.putField("TR_DT", requestData.getField("TR_DT"));
			rctmTrDetlRfctDsIn.putField("ACNO", requestData.getField("RCTM_ACNO"));
			rctmTrDetlRfctDsIn.putField("TR_NO", requestData.getLongField("RCTM_TR_SN"));
			rctmTrDetlRfctDsIn.putField("SYNS_CD", requestData.getField("RCTM_SYNS_CD"));
			rctmTrDetlRfctDsIn.putField("TR_AMT", requestData.getLongField("ATLN_AMT"));
			rctmTrDetlRfctDsIn.putField("BF_DACA", cmDacaDrwgRfctDsOut.getLongField("BF_DACA"));
			rctmTrDetlRfctDsIn.putField("AF_DACA", cmDacaDrwgRfctDsOut.getLongField("AF_DACA"));
			rctmTrDetlRfctDsIn.putField("CNCL_YN", "N");
			rctmTrDetlRfctDsIn.putField("STRT_TR_NO", requestData.getLongField("RCTM_STRT_TR_NO"));
			
			
			rctmTrDetlRfctDsOut= rctmTrDetlRfct.cmTrDetlRfct(cmTrDetlRfctDsIn);
			/********************************************************************
			 *  출력자료 조립
			 ********************************************************************/
			
			
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
		
		/*일자체크*/
		if( StringUtils.isEmpty(requestData.getField("TR_DT"))) {
			logger.error("거래일자를 확인하세요.");
			throw new Exception("거래일자를 확인하세요.");
		}
		
		/*출금계좌번호체크*/
		if( StringUtils.isEmpty(requestData.getField("DRWG_ACNO"))
				|| StringUtils.length(requestData.getField("DRWG_ACNO")) != 11) {
			logger.error("출금계좌번호를 확인하세요.");
			throw new Exception("출금계좌번호를 확인하세요.");
		}
		
		/*출금거래번호*/
		if( requestData.getLongField("DRWG_TR_SN") <= 0) {
			logger.error("거래번호를 확인하세요.");
			throw new Exception("거래번호를 확인하세요.");
		}
	
		/*입금계좌번호체크*/
		if( StringUtils.isEmpty(requestData.getField("RCTM_ACNO"))
				|| StringUtils.length(requestData.getField("RCTM_ACNO")) != 11) {
			logger.error("입금계좌번호를 확인하세요.");
			throw new Exception("입금계좌번호를 확인하세요.");
		}
		
		/*입금거래번호*/
		if( requestData.getLongField("RCTM_TR_SN") <= 0) {
			logger.error("입금거래번호를 확인하세요.");
			throw new Exception("입금거래번호를 확인하세요.");
		}

		/*대체금액*/
		if( requestData.getLongField("ATLN_AMT") <= 0) {
			logger.error("대체금액을 확인하세요.");
			throw new Exception("대체금액을 확인하세요.");
		}
		
		/*적요코드체크*/
		if( StringUtils.isEmpty(requestData.getField("DRWG_SYNS_CD"))
				||StringUtils.length(requestData.getField("DRWG_SYNS_CD")) != 3) {
			logger.error("출금적요코드를 확인하세요.");
			throw new Exception("출금적요코드를 확인하세요.");
		}
		
		/*적요코드체크*/
		if( StringUtils.isEmpty(requestData.getField("RCTM_SYNS_CD"))
				||StringUtils.length(requestData.getField("RCTM_SYNS_CD")) != 3) {
			logger.error("입금적요코드를 확인하세요.");
			throw new Exception("입금적요코드를 확인하세요.");
		}
	}
	
}
