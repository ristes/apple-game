Game.controller('SprayingController', [
  '$scope',
  'Store',
  'BoughtItems',
  '$day',
  'Spraying',
  function($scope, Store, BoughtItems, $day, Spraying) {

    var showProgress = function(_scope, oper, item) {
      Spraying.spray(item);

      $scope.$root.$emit('show-progress-global', {
        title: 'progress.' + oper.name,
        duration: oper.duration
      });
    };

    $scope.quantity = 1;

    var onUseSprayingItem = function(item) {
    	
    }
    
    var onBuyItem = function(item) {
      Store['buyItem']({
        itemName: item.name,
        quantity: item.perHa ? $scope.plantation.area : 1,
        currentState: $scope.$root.farmer.currentState
      }, null, function(result) {
        if (result.farmer.balans) {
          $day.load(result.farmer);
          BoughtItems.load(function() {
            $scope.$root.$emit('shop-hide');
            var boughtItems = BoughtItems.get($scope.sprayingOper.requires)[0];
            showProgress($scope, $scope.sprayingOper, boughtItems);
          });

        } else {
          $scope.$root.$emit('insuficient-funds');
        }
      }).$promise['finally'](function() {
    	  $scope.$root.$emit('item-bought');
      });
    };
    
    var unreg = $scope.$root.$on('operation-spraying', function(_s, oper) {
//      var hasItem = BoughtItems.check('spraying');
      $scope.sprayingOper = oper;
//      if (!hasItem) {
        $scope.$root.$emit('shop-show', {
          items: $scope.$root.storeItems[oper.requires],
          showNext: true,
          storeUrl: oper.ico,
          onItemClick: onBuyItem
        });

//      } else {
//        var types = BoughtItems.get(oper.requires);
//        if (types.length > 1) {
//          $scope.$root.$emit('shop-show', {
//            items: types,
//            showNext: true,
//            storeUrl: oper.ico,
//            shop: {
//              name: oper.requires
//            },
//            onItemClick: function(t) {
//              $scope.type = t;
//              $scope.$root.$emit('shop-hide');
//              showProgress($scope, oper, t);
//            }
//          });
//        } else {
//          $scope.type = types[0];
//          showProgress($scope, oper, types[0]);
//        }
//      }

    });

    $scope.$on("$destroy", function() {
      if (unreg) {
        unreg();
      }
    });

  }
]);
