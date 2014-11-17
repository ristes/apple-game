package service;

import service.impl.BadgesServiceImpl;
import service.impl.ContextServiceImpl;

public class ServiceInjector {

	public static BadgesService badgesService = new BadgesServiceImpl();
	public static ContextService contextService = new ContextServiceImpl();

}
