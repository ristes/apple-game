Game.factory('Irrigate', [
  '$http',
  '$day',
  'Store',
  function($http, $day, Store) {
    var GROOVES = 'grooves';
    var DROPS = 'drops';
    var TENSIOMETERS = 'tensiometers';


    var nextType = {
      grooves: DROPS,
      drops: TENSIOMETERS
    };
    var mmPerHour = {
      grooves: 1,
      drops: 0.75
    }
    return {

      tensiometerTime: function(type) {
        var res = $http.get("/IrrigationController/tensiometerTime");
        return res;
      },
      dropsIrrigation: function(time) {
        var res = $http.get("/IrrigationController/dropsIrrigation?time=" + time);
        res.success(function(data) {
          $day.load(data);
        });
      },
      groovesIrrigation: function(time) {
        var res = $http.get("/IrrigationController/groovesIrrigation?time=" + time);
        res.success(function(data) {
          $day.load(data);
        });
      },
      nextIrrigationType: function(type) {
        if (type.hasTensiometer) {
          return null;
        }
        var res = nextType[type.name];
        if (res == TENSIOMETERS && $day.get().gameDate.dayOrder < 600) {
          return null;
        } else {
          return res;
        }

      },
      getTypeAsync: function(callback) {

        var res = $http.get("/IrrigationController/activeIrrigationItem");
        res.success(function(data) {
          if (data.name == 'tensiometers') {
            data.name = 'drops';
            data.hasTensiometer = true;
          }
          data.mmPerHour = mmPerHour[data.name];
          irrigationType = data;
          callback(data);
        });
      },
      buy: function(irrigationName, callback) {
        var self = this;
        Store.buy({
          itemName: irrigationName,
          quantity: 1
        }, null, function(data) {
          $day.load(data);
          self.getTypeAsync(callback);
        });
      }
    };
  }
]);
