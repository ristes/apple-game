Game.controller('PlantingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$plantation',
    '$weather',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather) {

      $weather.load();
      $plantation.load();

      var onBuyItem = function(_scope, item) {
        Store['buyItem']({
          itemId: item.id,
          size: plantation.area
        }, null, function(result) {
          if (result.balans) {
            $items.add(item.store, item);
            $farmer.swap(result);
            $scope.$root.$emit('shop-hide');
          } else {
            $scope.$root.$emit('insuficient-funds');
          }
        }).$promise['finally'](function() {
          $scope.$root.$emit('item-bought');

        });
      };

      var showStoreItems = function(_scope, store) {
        if ($scope.unreg) {
          $scope.unreg();
          delete $scope.unreg;
        }
        $scope.$root.$emit('shop-hide');
        $scope.$root.$emit('item-bought');
        $scope.$root.$emit('shop-show', {
          items: StoreItems[store.name],
          showNext: true,
          storeUrl: '/public/images/game/store' + store.name + '.png'
        });
        $scope.unreg = $scope.$root.$on('buy-item', onBuyItem);

      };

      var showStores = function(_scope, oper) {
        $scope.$root.$emit('shop-show', {
          items: StoreItems['stores'],
          showNext: true,
          storeUrl: '/public/images/game/sadnici-icon.png'
        });
        if ($scope.unreg) {
          $scope.unreg();
        }
        $scope.unreg = $scope.$root.$on('buy-item', showStoreItems);

      };

      $scope.un = $scope.$root.$on("shop-hide", function() {
        if ($scope.unreg) {
          $scope.unreg();
          delete $scope.unreg;
        }
      });

      $scope.$on("$destroy", function() {
        $scope.un();
        delete $scope.un;
      });

      var showProgress = function(_scope, oper) {
        $scope.$root.$emit('show-progress-global', {
          title: 'progress.' + oper.name,
          duration: oper.duration
        });
      };

      var progress = function(_scope, oper) {

        var clear = function() {
          if ($scope.unregBought) {
            $scope.unregBought();
            delete $scope.unregBought;
          }
          if ($scope.unregHide) {
            $scope.unregHide();
            delete $scope.unregHide;
          }
        }
        if (oper.duration > 0) {
          if (oper.requires) {
            var hasItem = $items.check(oper.requires);
            if (!hasItem) {
              $scope.unregHide = $scope.$root
                      .$on('hide-progress-global', clear);

              $scope.$root.$emit('shop-show', {
                items: StoreItems[oper.requires],
                showNext: false,
                storeUrl: '/public/images/game/store-' + oper.requires + '.png'
              });

              $scope.unreg = $scope.$root.$on('buy-item', onBuyItem);
              $scope.unregBought = $scope.$root.$on('item-bought', function() {
                showProgress($scope, oper);
                $scope.$root.$emit('shop-hide');
              });

            }
          } else {
            showProgress($scope, oper);
          }
        }
      };

      var operationListeners = {
        'store': showStores
      };

      $scope.actions = Operations['planting'];

      var listeners = [];
      for (var i = 0; i < $scope.actions.length; i++) {
        var a = $scope.actions[i]

        if (operationListeners[a.name]) {
          var unreg = $scope.$root.$on('operation-' + a.name,
                  operationListeners[a.name]);
          listeners.push(unreg);
        } else {
          var unreg = $scope.$root.$on('operation-' + a.name, progress);
          listeners.push(unreg);
        }
      }
      $scope.$on("$destroy", function() {
        for (var i = 0; i < listeners.length; i++) {
          listeners[i]();
        }
      });

    }]);