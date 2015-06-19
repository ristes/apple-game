package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Farmer;
import models.LogFarmerData;
import models.Yield;
import service.ResumeService;
import service.ServiceInjector;
import dto.ResumeMessageDto;

public class ResumeServiceImpl implements ResumeService {

	@Override
	public ResumeMessageDto moneySpent(Farmer farmer, Integer year) {
		Integer sum = 0;
		ResumeMessageDto<List<String>> dto = new ResumeMessageDto<List<String>>();
		dto.message = new ArrayList<String>();
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.MONEY_SPENT).fetch();
		for (LogFarmerData log : logs) {
			sum += log.information.intValue();
		}
		dto.message.add(sum.toString());
		dto.typeResume = ResumeService.MONEY_SPENT;
		return dto;
	}

	@Override
	public ResumeMessageDto moneyEarned(Farmer farmer, Integer year) {
		Integer sum = 0;
		ResumeMessageDto<List<String>> dto = new ResumeMessageDto<List<String>>();
		dto.message = new ArrayList<String>();
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.MONEY_EARNED).fetch();
		for (LogFarmerData log : logs) {
			sum += log.information.intValue();
		}
		dto.message.add(sum.toString());
		dto.typeResume = ResumeService.MONEY_EARNED;
		return dto;
	}
	
	public Integer calcMoneyEarned(Farmer farmer, Integer year) {
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.MONEY_EARNED).fetch();
		Integer sum = 0;
		for (LogFarmerData log : logs) {
			sum += log.information.intValue();
		}
		return sum;
	}

	@Override
	public ResumeMessageDto operationsExecuted(Farmer farmer, Integer year) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResumeMessageDto<Map<String, Integer>> dto = new ResumeMessageDto<Map<String, Integer>>();
		dto.message = new HashMap<String, Integer>();
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.OPERATION_EXECUTED)
				.fetch();
		for (LogFarmerData log : logs) {
			if (log.operation != null) {
				if (map.containsKey(log.operation.name)) {
					map.put(log.operation.name,
							new Integer(map.get(log.operation.name) + 1));
				} else {
					map.put(log.operation.name, 1);
				}
			}
		}
		dto.message = map;
		dto.typeResume = ResumeService.MONEY_SPENT;
		return dto;
	}

	@Override
	public ResumeMessageDto diseasesOccured(Farmer farmer, Integer year) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResumeMessageDto<Map<String, Integer>> dto = new ResumeMessageDto<Map<String, Integer>>();
		dto.message = new HashMap<String, Integer>();
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.DISEASES_OCCURED).fetch();
		for (LogFarmerData log : logs) {
			if (map.containsKey(log.disease.name)) {
				map.put(log.disease.name, new Integer(map.get(log.disease.name)
						+ log.information.intValue()));
			} else {
				map.put(log.disease.name, log.information.intValue());
			}
		}
		dto.message = map;
		dto.typeResume = ResumeService.DISEASES_OCCURED;
		return dto;
	}

	@Override
	public ResumeMessageDto applesSold(Farmer farmer, Integer year) {
		Integer sum = 0;
		ResumeMessageDto<List<String>> dto = new ResumeMessageDto<List<String>>();
		dto.message = new ArrayList<String>();
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.APPLES_SOLD).fetch();
		for (LogFarmerData log : logs) {
			sum += log.information.intValue();
		}
		dto.message.add(sum.toString());
		dto.typeResume = ResumeService.APPLES_SOLD;
		return dto;
	}

	@Override
	public ResumeMessageDto applesBurned(Farmer farmer, Integer year) {
		Integer sum = 0;
		ResumeMessageDto<List<String>> dto = new ResumeMessageDto<List<String>>();
		dto.message = new ArrayList<String>();
		List<LogFarmerData> logs = LogFarmerData.find(
				"byFarmerAndRecolteYearAndTypelog", farmer, year,
				ServiceInjector.logFarmerDataService.APPLES_BURNT).fetch();
		for (LogFarmerData log : logs) {
			sum += log.information.intValue();
		}
		dto.message.add(sum.toString());
		dto.typeResume = ResumeService.APPLES_BURNT;
		return dto;
	}

	@Override
	public Integer calculateYield(Farmer farmer, int recolte) {
		List<Yield> yields = Yield.find("farmer=?1 and year=?2", farmer, recolte).fetch();
		Integer sum = 0;
		for (Yield yield:yields) {
			sum+=yield.quantity;
		}
		return sum;
	}

}
