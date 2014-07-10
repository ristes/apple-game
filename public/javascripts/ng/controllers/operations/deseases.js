Game.controller('DeseaseController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    '$interval',
    '$timeout',
    '$irrigate',
    'jQuery',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather, $interval, $timeout, $irrigate, $) {

      $scope.visible = false;
      $scope.url = '/public/images/game/operations/desease-analysis.png';

      $scope.hide = function() {
        $scope.visible = false;
      }

      var unreg = $scope.$root.$on('operation-desease-analysis', function(_s,
              oper) {
        $scope.hasDecease = false;
        var luck = $scope.$root.day.luck;
        $scope.deceases = [];
        if ($scope.$root.diseases) {
        	/*
			 * for ( var i in $scope.$root.diseases) { var d =
			 * $scope.$root.diseases[i]; if (d.probability < luck) {
			 * $scope.deceases.push({ name: d.name, url:
			 * '/public/images/game/deceases/' + d.name + '.png' });
			 * $scope.hasDecease = true; } }
			 */
        	if ($scope.$root.diseases.length!=0) {
        		$scope.hasDecease = true;
        		for (var i=0; i < $scope.$root.diseases.length;i++) {
        			var d = $scope.$root.diseases[i];
        			$scope.deceases.push({
        				name: d,
        				url: '/public/images/game/deceases/' + d + '.png'
        			});
        		}
        	}
        }

        $scope.visible = true;

      });

      var unregHide = $scope.$root.$on('shop-hide', function() {
        $scope.visible = false;
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
        if (unregHide) {
          unregHide();
        }
      });

      function extractIrrigationItems() {
        var types = $items.get('irrigation');

        var result = [];

        for (var i = 0; i < types.length; i++) {
          if (types[i].name == 'tensiometer') {
            $scope.hasTensiometer = true;
          } else {
            result.push(types[i]);
          }
        }

        return result;

      }

      $scope.cfg = {
        range: "max",
        min: 0,
        max: 25,
        slide: function(event, ui) {
          $scope.$apply(function() {
            $scope.holder.duration = ui.value;
          });
        }
      };

    }]);
