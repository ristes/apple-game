package dao;

import dao.impl.DeceasesDaoImpl;
import dao.impl.FertilizingDaoImpl;
import dao.impl.FieldDaoImpl;
import dao.impl.ItemsDaoImpl;
import dao.impl.SeedlingDaoImpl;

public class DaoInjector {
	
	public static DeceasesDao deceasesDao = new DeceasesDaoImpl();
	public static FertilizingDao fertilizingDao = new FertilizingDaoImpl();
	public static FieldDao fieldDao = new FieldDaoImpl();
	public static ItemsDao itemsDao = new ItemsDaoImpl();
	public static SeedlingDao seedlingDao = new SeedlingDaoImpl();
	
}
