package service.impl;

import service.FieldService;
import dao.FieldDao;
import dao.impl.FieldDaoImpl;
import models.Farmer;

public class FieldServiceImpl implements FieldService{

	@Override
	public Boolean hasTensiometerSystem(Farmer farmer) {
		FieldDao fieldDao = new FieldDaoImpl();
		return fieldDao.hasTensiometerSystem(farmer);
	}

	@Override
	public Boolean hasDropSystem(Farmer farmer) {
		FieldDao fieldDao = new FieldDaoImpl();
		return fieldDao.hasTensiometerSystem(farmer);
	}
	
}
