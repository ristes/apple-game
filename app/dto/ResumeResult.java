package dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeResult {
	
	public Map<String,ResumeMessageDto<List<String>>> intTypes;
	
	public Map<String,ResumeMessageDto<Map<String,Integer>>> hashTypes;
	
	public ResumeResult() {
		intTypes = new HashMap<String,ResumeMessageDto<List<String>>>();
		hashTypes = new HashMap<String,ResumeMessageDto<Map<String,Integer>>>();
	}

}
