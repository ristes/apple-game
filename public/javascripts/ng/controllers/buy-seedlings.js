Game.controller('BuySeadlingsController', ['$scope', 'Planting',
    function($scope, Planting) {

      $scope.seedlings = [];
      $scope.appleTypes = [];
      $scope.seedlingTypes = [];
      $scope.bases = [];

      $scope.selected = {};
      $scope.showBase = false;
      $scope.seedlingChosen = false;
      $scope.count = 0;

      $scope.total = function() {
        var total = 0;
        angular.forEach($scope.selected, function(val) {
          total += val.quantity * val.price;
        });
        return total;
      }

      $scope.select = function(seedling) {
        var key = seedling.type.id;
        var q = 0;
        if ($scope.selected[key]) {
          q = $scope.selected[key].quantity;
        }
        seedling.quantity = q;
        if ($scope.count >= 3 && !$scope.selected.hasOwnProperty(key)) {
          alert('To many apple types. Remove some in order to add new')
        } else {
          if (!$scope.selected.hasOwnProperty(key)) {
            $scope.count++;
          }
          $scope.selected[key] = seedling;
        }
      };

      $scope.removeSelected = function(seedling) {
        var key = seedling.type.id;
        if ($scope.selected.hasOwnProperty(key)) {
          $scope.count--;
        }
        delete $scope.selected[key];
      };

      $scope.buySeedlings = function() {
        var bought = [];
        angular.forEach($scope.selected, function(val) {
          bought.push(val);
        });
        Planting.buySeedlings(bought, function() {
          $scope.showBase = true;
        });
      };

      Planting.seedlings(function(seedlings) {
        var at = {};
        var st = {};
        // seedlings
        $scope.seedlings = seedlings;
        angular.forEach(seedlings, function(val) {
          at[val.type.id] = val.type;
          st[val.seedlingType.id] = val.seedlingType;
        });
        // plantTypes
        $scope.appleTypes = [];
        angular.forEach(at, function(val) {
          $scope.appleTypes.push(val);
        });
        // seedlingTypes
        $scope.seedlingTypes = [];
        angular.forEach(st, function(val) {
          $scope.seedlingTypes.push(val);
        });
      });

    }]);
