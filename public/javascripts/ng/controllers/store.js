Game.controller('StoreController', ['$scope', '$translate', '$http', 'Store',
  'StoreItems', 'BoughtItems', '$farmer', '$day',
  function($scope, $translate, $http, Store, StoreItems, BoughtItems, $farmer, $day) {

    $scope.initStore = function(store, nextState, servMethod, shopIcon) {
      $scope.nextState = nextState;
      $scope.servMethod = servMethod || 'buy';
      $scope.store = store;
      $scope.items = StoreItems.getStoreItems()[store];

      $scope.$root.$emit('shop-show', {
        items: $scope.items,
        showNext: false,
        storeUrl: shopIcon,
        shop: {
          name: store
        }
      });
    };

    $scope.onBuyItem = function(_scope, item) {
      Store[$scope.servMethod]({
        itemid: item.id,
        itemName: item.name,
        quantity: 1,
        price: item.price,
        currentState: "/" + $scope.nextState
      }, null, function(result) {
        if (result.balans) {
          $farmer.swap(result);
          $day.load(result);
          BoughtItems.add($scope.store, item);
          $scope.$root.$emit('shop-hide');
        } else {
          $scope.$root.$emit('insuficient-funds');
        }
      }).$promise['finally'](function() {
        $scope.$root.$emit('item-bought');
      });
    };

    var unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

    $scope.$on("$destroy", function() {
      unreg();
    })

  }
]);
