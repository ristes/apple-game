Game.controller('ShoppingController', [
		'$scope',
		'Store',
		'StoreItems',
		'State',
		'BoughtItems',
		'Plantation',
		'Toaster',
		function($scope, Store, StoreItems, State, BoughtItems, Plantation,
				Toaster) {

			Plantation.load();

			var onBuyItem = function(item) {
				Store['buyItem']({
					itemName : item.name,
					quantity : item.perHa ? $scope.plantation.area : 1,
					currentState : State.gameState().currentState
				}, null, function(result) {
					if (result.status) {
						BoughtItems.load();
						StoreItems.load();
						State.set("farmer", result.farmer);
						$scope.$root.$emit('shop-hide');
						Toaster.success("Item bought.");
					} else {
						$scope.$root.$emit('insuficient-funds');
					}
				}).$promise['finally'](function() {
					$scope.$root.$emit('item-bought');

				});
			};

			var showStoreItems = function(store) {
				$scope.$root.$emit('shop-hide');
				$scope.$root.$emit('item-bought');
				$scope.store = store;
				$scope.$root.$emit('shop-show', {
					items : StoreItems.getStoreItems()[store.name],
					showNext : true,
					shop : store,
					storeUrl : store.url,
					onItemClick : onBuyItem

				});
			};

			var unreg = $scope.$root.$on('operation-store', function() {
				StoreItems.getItemsFromOther(function(data) {

					$scope.$root.$emit('shop-show', {
						items : data,
						showNext : true,
						shop : {
							name : 'shop'
						},
						storeUrl : '/public/images/game/operations/shop.png',
						onItemClick : onBuyItem
					});
				});
			});

			$scope.$on("$destroy", function() {
				if (unreg) {
					unreg();
				}

			});

		} ]);
