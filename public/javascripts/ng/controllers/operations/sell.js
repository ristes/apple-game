Game
		.controller(
				'SellController',
				[
						'$scope',
						'$translate',
						'Fridge',
						'State',
						'Harvesting',
						'ExpertAdvice',
						'$day',
						function($scope, $translate, Fridge, State, Harvesting,
								ExpertAdvice, $day) {

							$scope.farmer = $day.get();
							var SliderCfg = function(max, min) {
								this.range = "max";
								this.max = max || 0;
								this.min = min || 0;
								this.quantity = 0;
								var self = this;

								this.slide = function(event, ui) {
									$scope.$apply(function() {
										self.quantity = ui.value;
									});
								}
							};

							$scope.onHelp = function() {
								ExpertAdvice
										.setInfinteImportantAdvice("In general, apples can be stored for a long period of time (up to 12 months). A variety of factors influence its shelf life, such as its variety, quality, region of production and mode of storage. Fruit shelf life in storehouses depends on air humidity, temperature and air composition. Apple storage can be in ordinary storehouses without an installed cooling system, refrigerators with normal ambient cooling, refrigerators with controlled ambient cooling, and refrigerators with dynamic ambient cooling. Temperature of 0-3 0С and air relative humidity of 92–93 % ensures optimal conditions for storage of the majority of apple varieties. Modern storage systems (controlled ambient) additionally reduce oxygen concentration while increasing that of carbon dioxide, which makes the effect of fruit storage even more effective.");
							}

							var unreg = $scope.$root
									.$on(
											'operation-sell',
											function(_s, oper) {
												$scope.farmer = $day.get();
												$scope.type = {
													ico : '/public/images/game/operations/shop.png',
													name : 'sell'
												};

												$scope.storeCfg = [];

												$scope.visible = true;
												$scope.$root.$emit("side-hide");
												$scope.plantation = State
														.getByField("plantation");

												for (var i = 0; i < $scope.plantation.plantTypes.length; i++) {
													var type = $scope.plantation.plantTypes[i];
													$scope.storeCfg[type.name] = new SliderCfg(
															0)
												}

												$scope.inintStoring();
											});

							$scope.inintStoring = function() {

								$scope.fridges = Fridge.load();

								Harvesting
										.getYield(function(data) {
											$scope.yields = data;
											for (var i = 0; i < data.length; i++) {
												var yield = data[i];
												var plantTypeName = yield.plantationSeedling.seedling.type.name;

												$scope.storeCfg[plantTypeName] = new SliderCfg(
														yield.quantity
																- yield.storedQuantity);
											}
										});
							};

							$scope.$on("$destroy", function() {
								if (unreg) {
									unreg();
								}
							});

							$scope.buyCapacity = function(fridge) {
								Fridge
										.buyCapacity(
												fridge.fridgeType,
												$scope.fridgesCapacity[fridge.fridgeType].capacity,
												$scope.inintStoring);
							};

							$scope.store = function(fridge, plantType, cfg) {
								var quantity = cfg.quantity;
								var plantType = plantType.id;

								Fridge.addtofridge(fridge.fridgeType,
										plantType, quantity,
										$scope.inintStoring);
							};

							$scope.changeQuantity = function(shelf) {
								if (!shelf.changeQuantity) {
									shelf.changeQuantity = true;
									shelf.sliderQuantity = 0;
									shelf.cfg = {
										range : "max",
										min : 1,
										max : shelf.quantity,
										slide : function(event, ui) {
											$scope
													.$apply(function() {
														shelf.sliderQuantity = ui.value;
													});
										}
									}
								}
							};
							
							var successfulApplesSold = function(quantity,money) {
								ExpertAdvice
								.setInfinteImportantAdvice("Congratulations! You sold:"+quantity+" kg apples and earned: "+parseInt(money)+" coins");
								$scope.inintStoring();
							}

							$scope.sell = function(type, cfg) {
								Harvesting.sell(type.id, cfg.quantity,
										successfulApplesSold);
							};

							$scope.removeQuantity = function(shelf, fridge) {
								shelf.quantity -= shelf.sliderQuantity;
								Fridge.removeFromFridge(fridge.fridgeType,
										shelf.plantType.id,
										shelf.sliderQuantity,
										$scope.inintStoring);
								shelf.changeQuantity = false;
								$scope.storeCfg[shelf.plantType.name] = new SliderCfg(
										shelf.sliderQuantity);
//								$scope.sell(shelf.plantType, {quantity:shelf.sliderQuantity});
							};
							
//							$scope.sell = function() {
//								$scope.sell(shelf.plantType, {quantity:shelf.sliderQuantity});
//							}

							$scope.fridgesCapacity = [ {
								capacity : 0
							}, {
								capacity : 0
							}, {
								capacity : 0
							} ];

							$scope.cfg = [
									{
										range : "max",
										min : 1,
										step : 100,
										max : 1000,
										slide : function(event, ui) {
											$scope
													.$apply(function() {
														$scope.fridgesCapacity[0].capacity = ui.value;
													});
										}
									},
									{
										range : "max",
										min : 0,
										step : 100,
										max : 1000,
										slide : function(event, ui) {
											$scope
													.$apply(function() {
														$scope.fridgesCapacity[1].capacity = ui.value;
													});
										}
									},
									{
										range : "max",
										min : 0,
										max : 1000,
										slide : function(event, ui) {
											$scope
													.$apply(function() {
														$scope.fridgesCapacity[2].capacity = ui.value;
													});
										}
									} ];

							$scope.hide = function() {
								$scope.visible = false;
							};

						} ]);

Game.controller("SellButtonController", ["$scope", function($scope) {
	$scope.sell = function() {
		$scope.$root.$emit('operation-sell');
	}
}])
