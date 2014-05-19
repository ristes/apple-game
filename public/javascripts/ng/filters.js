'use strict';

/* Filters */

angular.module('Game.filters', []).filter('fileSize', function() {
  return function(fileSizeInBytes) {
    var i = -1;
    var byteUnits = [' kB', ' MB', ' GB', ' TB', 'PB', 'EB', 'ZB', 'YB'];
    do {
      fileSizeInBytes = fileSizeInBytes / 1024;
      i++;
    } while (fileSizeInBytes > 1024);

    return Math.max(fileSizeInBytes, 0.1).toFixed(1) + byteUnits[i];
  };
}).filter('integer', function() {
  return function(number) {
    return Math.round(number);
  };
});
