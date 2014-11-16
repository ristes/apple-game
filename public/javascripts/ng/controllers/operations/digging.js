Game
		.controller(
				'DiggingController',
				[
						'$scope',
						'Plowing',
						'Store',
						'$day',
						'$items',
						function($scope, Plowing, Store, $day, $items) {

							var unreg = $scope.$root
									.$on(
											'operation-digging',
											function(_s, oper) {

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
																			$day.load(result.farmer);
																			$scope.$root.$emit('item-bought');
																			Plowing.digging(item.id, function() {
																				$scope.$root.$emit('item-bought');
																				$scope.$root.$emit('show-progress-global',{
																					title : 'progress.digging',
																					duration : 5
																				});
																			});

																			$items.load();
																			

																		} else {
																			$scope.$root
																					.$emit('insuficient-funds');
																		}
																	}).$promise['finally'](function() {
																		$scope.$root.$emit('item-bought');

																	});
													};

												$scope.$root
														.$emit(
																'shop-show',
																{
																	items : $scope.$root.storeItems[oper.requires],
																	showNext : true,
																	storeUrl : oper.ico,
																	onItemClick : onBuyItem
																});
												/*
												 * Plowing.digging(function() {
												 * $scope.$root.$emit('show-progress-global', {
												 * title: 'progress.digging',
												 * duration: 5 }); });
												 */

											});

							$scope.$on("$destroy", function() {
								if (unreg) {
									unreg();
								}
							});

						} ]);