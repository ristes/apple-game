Game
		.controller(
				'FertilizingController',
				[
						'$scope',
						'Fertilize',
						'State',
						'Planting',
						'Store',
						'BoughtItems',
						'$timeout',
						'$interval',
						'$window',
						'ExpertAdvice',
						function($scope, Fertilize, State, Planting, Store,
								BoughtItems, $timeout, $interval, $window,
								ExpertAdvice) {

							// $scope.fertilizer = {
							// n: 40,
							// p: 40,
							// k: 40,
							// ca: 9,
							// mg: 3,
							// b: 0.35
							// };

							

							Fertilize.recommend(function(data) {
								data = _.groupBy(data, function(
										item) {
									item.immutable = true;
									return item.name;
								});
								$scope.fertilizer = new Object();
								$scope.fertilizer['N'] = new Array();
								$scope.fertilizer['P'] = new Array();
								$scope.fertilizer['K'] = new Array();
								$scope.fertilizer['Ca'] = new Array();
								$scope.fertilizer['Mg'] = new Array();
								$scope.fertilizer['B'] = new Array();
								$scope.fertilizer['Zn'] = new Array();
								$scope.fertilizer['N'] = data.N || [{'name':'N','value':0}];
								$scope.fertilizer.P = data.P || [{'name':'P','value':0}];
								$scope.fertilizer.K = data.K || [{'name':'K','value':0}];
								$scope.fertilizer.Ca = data.Ca || [{'name':'Ca','value':0}];
								$scope.fertilizer.Mg = data.Mg || [{'name':'Mg','value':0}];
								$scope.fertilizer.B = data.B || [{'name':'B','value':0}];
								$scope.fertilizer.Zn = data.Zn || [{'name':'Zn','value':0}];
							});
							
							$scope.onGlobalHelp = function() {
								ExpertAdvice
										.setInfinteImportantAdvice("In order to achieve a productive and high-quality apple yield, the soil must contain an adequate quantity of nutrients, as well as being as neutral (in terms of pH) as possible. Nutrients are divided into macro-elements (nitrogen, phosphorus, potassium, magnesium, calcium) and micro-elements (copper, zinc, boron, manganese, iron, and molybdenum). A plantation’s soil should be analyzed every 4-5 years.");
							}
							
							$scope.buyHelpFertilizing = function() {
								
							}
							function calculatePrice() {
								var n = $scope.fertilizer;
								var p = $scope.prices;
								$scope.price = n.N[0].value * p.n
										+ n.P[0].value * p.p + n.K[0].value
										* p.k + n.Ca[0].value * p.ca
										+ n.B[0].value * p.b + n.Mg[0].value
										* p.mg + n.Zn[0].value * p.zn;
							}

							$scope.onHelp = function(item) {
								ExpertAdvice
										.setInfinteImportantAdvice(Fertilize[item].description);
							}

							$scope.nCfg = {
								range : "max",
								min : 0,
								max : 60,
								onChange : function(n) {
									$scope.fertilizer.N[0].value = n;
									calculatePrice();
								}
							};

							$scope.pCfg = {
								range : "max",
								min : 0,
								max : 60,
								onChange : function(n) {
									$scope.fertilizer.P[0].value = n;
									calculatePrice();
								}
							};

							$scope.kCfg = {
								range : "max",
								min : 0,
								max : 60,
								onChange : function(n) {
									$scope.fertilizer.K[0].value = n;
									calculatePrice();
								}
							};

							$scope.caCfg = {
								range : "max",
								min : 0,
								max : 18,
								onChange : function(n) {
									$scope.fertilizer.Ca[0].value = n;
									calculatePrice();
								}
							};

							$scope.bCfg = {
								range : "max",
								min : 0,
								max : 1,
								step : 0.05,
								onChange : function(n) {
									$scope.fertilizer.B[0].value = n;
									calculatePrice();
								}
							};

							$scope.mgCfg = {
								range : "max",
								min : 0,
								max : 10,
								onChange : function(n) {
									$scope.fertilizer.Mg[0].value = n;
									calculatePrice();
								}
							};

							$scope.znCfg = {
								range : "max",
								min : 0,
								max : 1,
								step : 0.05,
								onChange : function(n) {
									$scope.fertilizer.Zn[0].value = n;
									calculatePrice();
								}
							};

							$scope.fertilize = function() {

								var interval = 100;
								var time = 10 * 50;

								if (!$scope.fertilizeProgress) {
									Fertilize
											.fertilize(
													$scope.fertilizer,
													function() {

														$scope.status = 0;
//														$scope.fertilizeProgress = true;
														
														$scope.hide();

//														$interval(
//																function() {
//																	$scope.status += 100 / time;
//																	$scope.showStatus = Math
//																			.round($scope.status);
//																	if ($scope.status > 100) {
//																		$scope.status = 100;
//																	}
//
//																}, interval,
//																time);

//														$timeout(
//																function() {
//																	$scope.fertilizeProgress = false;
//																	$scope
//																			.hide();
//																},
//																interval
//																		* (time + 1));
														$scope.$root.$emit("show-animation-manager", {
									                    	name:"fertilizing"
									                    });
													});

								}
							}

							$scope.fertilizationUrl = '/public/images/game/operations/fertilizing.png';

							$scope.buyAnalisys = function() {
								Store
										.buyItem(
												{
													itemName : 'soil_analyse',
													quantity : 1,
													currentState : State
															.getByField("farmer").currentState
												},
												null,
												function(result) {
													BoughtItems
															.load(function() {
																$scope.analisysItem = BoughtItems
																		.getByName('soil_analyse');
															});
												});
							}

							$scope.hideAnalisys = function() {
								$scope.showingAnalysis = false;
							}
							$scope.showAnalisys = function() {
								Planting.analyseTerain($scope.analisysItem.id,
										function(data) {
											$scope.analysis = data;
											$scope.showingAnalysis = true;
										});
							}

							var unreg = $scope.$root.$on(
									'operation-fertilizing',
									function(_s, oper) {
										$scope.$root.$emit("side-hide");
										$scope.visible = true;

										var size = State
												.getByField("plantation").area;
										$scope.prices = {
											n : 0.5 * size,
											p : 0.5 * size,
											k : 0.5 * size,
											ca : 50 * size,
											mg : 50 * size,
											b : 50 * size,
											zn : 50 * size
										}
										calculatePrice();

										$scope.analisysItem = BoughtItems
												.getByName('soil_analyse');
										if ($scope.analisysItem) {
											$scope.haveAnalisys = true;
										}

										$scope.bgw = $window.innerWidth;
										$scope.bgh = $window.innerHeight;
									});

							$scope.hide = function() {
								$scope.visible = false;

								$scope.bgw = 0;
								$scope.bgh = 0;
							}

							$scope.$on("$destroy", function() {
								if (unreg) {
									unreg();
								}
							});

						} ]);
