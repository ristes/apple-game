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
    function($scope, $location, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather, toaster) {

      $weather.load();
      $plantation.load();

      $scope.actions = Operations['planting'];

      $scope.seedling = 0;
      $scope.totalSeedling = 0;
      
      var unreg = $scope.$root.$on('operation-planting-done', function(_s, oper) {
        var percent = ($scope.seedling * 100) / $scope.totalSeedling;
        console.log(percent);
        if (percent < 50) {
          percent = percent * 2;
          toaster.pop('warn', 'Not done yet', percent.toFixed(0)
                  + '% od sadenjeto e zavrseno');
        } else {
          toaster.pop('success', 'Done :)', 'sadenjeto e zavrseno');
        }

      });

      $scope.portionClick = function(p) {
        if (p.active && !p.tree) {
          p.tree = "/public/images/game/plant.png";
          p.treeCls = 'seedling';
          $scope.seedling++;
        }
      }

      $scope.rows = [];
      var N = 15, M = 5;
      var r = {
        cols: []
      };
      $scope.rows.push(r);
      for (var i = 0; i < N; i++) {
        r.cols.push({
          show: i >= M,
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
            show: (i + j >= M && i + j < N),
            active: (i + j >= M && i + j < N),
            x: i,
            y: j,
            terrain: '/public/images/pocva-riste.png',
            cls: 'pocva'
          })

        }

      }

      var r = {
        cols: []
      };
      $scope.rows.push(r);
      for (var i = 0; i < N - M; i++) {
        r.cols.push({
          show: true,
          x: i,
          y: j,
          terrain: '/public/images/ograda.png',
          cls: 'ograda-dole'
        })
      }

    }]);