Game.controller('PlantingStateController', [
    '$scope',
    '$location',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$plantation',
    '$weather',
    'toaster',
    '$window',
    '$day',
    function($scope, $location, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather, toaster, $window, $day) {


      $scope.onResizeFunction = function() {
        $scope.atom = $window.innerWidth * 0.8 / 20;
      };

      // Call to the function when the page is first loaded
      $scope.onResizeFunction();

      angular.element($window).bind('resize', function() {
        $scope.onResizeFunction();
        $scope.$apply();
      });
      $weather.load();
      $farmer.load();
      $scope.actions = Operations['planting'];

      if ($scope.$root.plantation == null) {
        $scope.coords = [];
      } else {
        $scope.coords = JSON.parse($scope.$root.plantation.treePositions);
      }
      $scope.seedling = $scope.coords.length;
      $scope.totalSeedling = $scope.coords.length;

      var isInCoords = function(p) {
        var arr = $scope.coords;

        for (var i = 0; i < arr.length; i++) {
          var el = arr[i];
          if (el.x === p.x && el.y === p.y) { return true; }
        }
        return false;
      }

      var unreg = $scope.$root.$on('operation-planting-done',
              function(_s, oper) {
                var percent = ($scope.seedling * 100) / $scope.totalSeedling;
                console.log(percent);
                // if (percent < 50) {
                // percent = percent * 2;
                // toaster.pop('warn', 'Not done yet', percent.toFixed(0)
                // + '% od sadenjeto e zavrseno');
                // } else {

                var data = $plantation.save(JSON.stringify($scope.coords),
                        'successPlanting');
                if (data.status == true) {
                  toaster.pop('success', 'Done :)', 'sadenjeto e zavrseno'
                          + JSON.stringify($scope.coords));
                }
                // }

              });

      var successPlanting = $scope.$root.$on("successPlanting", function() {
        toaster.pop('success', 'Done :)', 'sadenjeto e zavrseno');
      });

      $scope.portionClick = function(p) {
        if (p.active && !p.tree) {
          // p.tree = "/public/images/game/plant.png";
          p.tree = $scope.$root.farmer.plant_url;
          p.treeCls = 'seedling';
          $scope.seedling++;
          $scope.coords.push({
            x: p.x,
            y: p.y
          });
        }
      }

      $scope.rows = [];
      // var N = 15, M = 5;
      var N = 15, M = 5;
      var r = {
        cols: []
      };
      $scope.rows.push(r);
      for (var i = 0; i < N; i++) {
        r.cols.push({
          show: true,
          staticV: true,
          x: i,
          y: j,
          terrain: '/public/images/ograda.png',
          cls: 'ograda-gore'
        })
      }

      for (var j = 0; j < M; j++) {
        var r = {
          cols: []
        };
        $scope.rows.push(r);
        for (var i = 0; i < N; i++) {
          $scope.totalSeedling++;
          r.cols.push({
            show: true,
            active: true,
            staticV: false,
            x: i,
            y: j,
            terrain: $scope.$root.farmer.soil_url,
            cls: 'pocva'
          })

        }
        for (var i = 0; i < N; i++) {
          var p = r.cols[i];
          if (p.active && isInCoords(p)) {
            r.cols[i].tree = "/public/images/game/plant.png";
            r.cols[i].treeCls = 'seedling';
          }
        }

      }

    }]);