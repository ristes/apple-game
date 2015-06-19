Game
		.controller(
				'SprayingController',
				[
						'$scope',
						'Store',
						'BoughtItems',
						'$day',
						'Spraying',
						function($scope, Store, BoughtItems, $day, Spraying) {

							var showProgress = function(_scope, oper, item) {
								Spraying.spray(item);

								$scope.$root.$emit('show-progress-global', {
									title : 'progress.' + oper.name,
									duration : 3,
									actionToShare : 'Spraying'
								});
							};

							$scope.quantity = 1;

							var onUseSprayingItem = function(item) {

							}

							var onBuyItem = function(item) {
								Store['buyItem']
										(
												{
													itemName : item.name,
													quantity : item.perHa ? $scope.plantation.area
															: 1,
													currentState : $scope.$root.farmer.currentState
												},
												null,
												function(result) {
													if (result.farmer.balans) {
														$day.load(result);
														BoughtItems
																.load(function() {
																	$scope.$root
																			.$emit('shop-hide');
																	var boughtItems = BoughtItems
																			.get($scope.sprayingOper.requires)[0];
																	showProgress(
																			$scope,
																			$scope.sprayingOper,
																			result.t);
																});

													} else {
														$scope.$root
																.$emit('insuficient-funds');
													}
												}).$promise['finally']
										(function() {
											$scope.$root.$emit('item-bought');
										});
							};

							var unreg = $scope.$root
									.$on(
											'operation-spraying',
											function(_s, oper) {
												$scope.sprayingOper = oper;
												$scope.farmer = $day.get();

												$scope.$root
														.$emit(
																'shop-show',
																{
																	items : $scope.$root.storeItems[oper.requires],
																	showNext : true,
																	storeUrl : oper.ico,
																	onItemClick : onBuyItem
																});
											});

							$scope.$on("$destroy", function() {
								if (unreg) {
									unreg();
								}
							});

						} ]);
