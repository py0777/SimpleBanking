package sb.common;

import java.util.HashMap;
import java.util.Iterator;

import nexcore.framework.core.data.IRecord;
import nexcore.framework.core.data.IRecordSet;
import nexcore.framework.core.data.RecordSet;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class RecordSetResultHandler implements ResultHandler {

	private IRecordSet rs;
	private String recordSetId = "S1";
	private String[] headerNm;

	public RecordSetResultHandler() {
		
	}
	
	public String getRecordSetId() {
		return recordSetId;
	}

	public void setRecordSetId(String recordSetId) {
		this.recordSetId = recordSetId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext arg0) {
		
		HashMap<String , Object> hsm  = (HashMap<String, Object>) arg0.getResultObject();
		
		if (arg0.getResultCount() > 100000) 
			throw new RuntimeException("MAX Count reached...");

		if (arg0.getResultCount() == 1) {
			int tmp = 0;
			headerNm = new String[hsm.size()];
			for (Iterator<String> iterator = hsm.keySet().iterator(); iterator.hasNext();) {						
				headerNm[tmp++] = iterator.next();
			} 
			rs = new RecordSet(recordSetId, headerNm);
		}
		
		IRecord record = rs.newRecord();
		for (int i = 0; i < headerNm.length; i++) {
			record.set(headerNm[i], hsm.get(headerNm[i]));
		}
		
		
		rs.addRecord(record);	
	}
	
	public IRecordSet getRecordSet() {
		
		if (rs == null) 
			rs = new RecordSet(recordSetId);
		
		return rs;
	}

}
