package service.impl;

import models.Farmer;
import service.FieldService;
import dao.DaoInjector;
import dao.FieldDao;
import dao.impl.FieldDaoImpl;

public class FieldServiceImpl implements FieldService{

	@Override
	public Boolean hasTensiometerSystem(Farmer farmer) {
		return DaoInjector.fieldDao.hasTensiometerSystem(farmer);
	}

	@Override
	public Boolean hasDropSystem(Farmer farmer) {
		return DaoInjector.fieldDao.hasTensiometerSystem(farmer);
	}

	@Override
	public Boolean hasBees(Farmer farmer) {
		return DaoInjector.fieldDao.hasBees(farmer);
	}

	@Override
	public Boolean hasUVProtectingNet(Farmer farmer) {
		return DaoInjector.fieldDao.hasUVProtectingNet(farmer);
	}

	@Override
	public Boolean hasArtificialRain(Farmer farmer) {
		return DaoInjector.fieldDao.hasArtificalRain(farmer);
	}
	
}
