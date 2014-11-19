package service;

import service.impl.BadgesServiceImpl;
import service.impl.ContextServiceImpl;
import service.impl.DateServiceImpl;
import service.impl.DiseaseServiceImpl;
import service.impl.FarmerServiceImpl;
import service.impl.FertilizeServiceImpl;
import service.impl.FieldServiceImpl;
import service.impl.FridgeServiceImpl;
import service.impl.GrowingServiceImpl;
import service.impl.HarvestServiceImpl;
import service.impl.HumidityServiceImpl;
import service.impl.InfoTableServiceImpl;
import service.impl.InsuranceServiceImpl;
import service.impl.IrrigationServiceImpl;
import service.impl.LandTreatmanServiceImpl;
import service.impl.PlantationServiceImpl;
import service.impl.PlantingServiceImpl;
import service.impl.PriceServiceImpl;
import service.impl.RandomGeneratorServiceImpl;
import service.impl.SoilServiceImpl;
import service.impl.StoreServiceImpl;
import service.impl.ThiefServiceImpl;
import service.impl.TipServiceImpl;
import service.impl.TransactionServiceImpl;
import service.impl.YieldServiceImpl;
import service.impl.YmlServiceImpl;

public class ServiceInjector {

	public static BadgesService badgesService = new BadgesServiceImpl();
	public static ContextService contextService = new ContextServiceImpl();
	public static AppleSaleTransactionService appleSaleTransactionService = new TransactionServiceImpl();
	public static AppleTransactionService appleTransactionService = new TransactionServiceImpl();
	public static DateService dateService = new DateServiceImpl();
	public static DiseaseService diseaseService = new DiseaseServiceImpl();
	public static DispensibleItemTransaction dispensibleItemTransactionService = new TransactionServiceImpl();
	public static FarmerService farmerService = new FarmerServiceImpl();
	public static FertilizeService fertilizeService = new FertilizeServiceImpl();
	public static FieldService fieldService = new FieldServiceImpl();
	public static GrowingService growingService = new GrowingServiceImpl();
	public static HarvestService harvestService = new HarvestServiceImpl();
	public static HumidityDropsService humidityDropsService = new HumidityServiceImpl();
	public static HumidityGroovesService humidityGroovesService = new HumidityServiceImpl();
	public static HumidityService humidityService = new HumidityServiceImpl();
	public static IndispensibleItemTransaction indispensibleItemTrans = new TransactionServiceImpl();
	public static InfoTableService infoTableService = new InfoTableServiceImpl();
	public static InsuranceService insuranceService = new InsuranceServiceImpl();
	public static IrrigationService irrigationService = new IrrigationServiceImpl();
	public static ItemTransactionService itemTransactionService = new TransactionServiceImpl();
	public static LandTreatmanService landTreatmanService = new LandTreatmanServiceImpl();
	public static MoneyTransactionService moneyTransactionService = new TransactionServiceImpl();
	public static PlantingService plantingService = new PlantingServiceImpl();
	public static PriceService priceService = new PriceServiceImpl();
	public static RandomGeneratorService randomGeneratorService = new RandomGeneratorServiceImpl();
	public static SoilService soilService = new SoilServiceImpl();
	public static StoreService storeService = new StoreServiceImpl();
	public static ThiefService thiefService = new ThiefServiceImpl();
	public static TipService tipService = new TipServiceImpl();
	public static YieldService yieldService = new YieldServiceImpl();
	public static YmlService ymlService = new YmlServiceImpl();
	public static PlantationService plantationService = new PlantationServiceImpl();
	public static FridgeService fridgeService = new FridgeServiceImpl();
	

}
