Game.controller('HarvestingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    '$day',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather, $day) {

    	  
    	  //TODO: open panel to choose weather to play the game or not
    	  
    	  // not playing => call service with score = 0.7
    	  // otherwise show game panel, and disable clicking anywhere else
    	/*
        $scope.$root.$emit('show-progress-global', {
          title: 'progress.harvest',
          duration: oper.duration
        });
        
        
        var res = $http.get("/HarvestingController/harvest");
        
        res.success(function(data) {
          $day.load(data.farmer);
          var res1 = $http.get("/salecontroller/sale?quantity="
                  + data.farmer.apples_in_stock);
          res.success(function(data) {
            $day.load(data.farmer);
          });
        });
        $scope.$on("$destroy", function() {
            if (unreg) {
              unreg();
            }
          });
      }]);

    

