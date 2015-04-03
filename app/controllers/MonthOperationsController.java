package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.MonthOperation;
import models.Operation;

public class MonthOperationsController extends JsonController{
	
	public static void all() {
		Map<String, List<Operation>> map = new HashMap<String,List<Operation>>();
		List<MonthOperation> all = MonthOperation.findAll();
		for (MonthOperation operation:all) {
			if (map.containsKey("month-"+String.valueOf(operation.month))) {
				map.get("month-"+String.valueOf(operation.month)).add(operation.operation);
			} else {
				List<Operation> ops = new ArrayList<Operation>();
				ops.add(operation.operation);
				map.put("month-"+String.valueOf(operation.month), ops);
			}
			
		}
		renderJSON(map);
	}

}
