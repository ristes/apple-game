Land = function(columns, rows, firstRow, iso, atom) {

  Crafty.sprite(128, "/public/images/sprite.png", {
    grass: [0, 0, 1, 1], // x, y, w, h
    stone: [1, 0, 1, 1]
  });

  var field = {};

  this.field = field;

  function checkInside(i, y) {
    var p = i + (y + 1) / 2;
    var bord = (y % 2 == 1 && p == 15);

    var inside = (p <= 15 && p >= 8) && y >= 7 && y <= 16 && !bord;
    return inside;
  }

  function createTile(i, y, size, type) {

    var z = (columns - i) + y * (columns + 1);

    var inside = checkInside(i, y);

    var type = type || "grass";
    if (inside) {

      var over = function() {
        // this.sprite(0, 1, 1, 1);
      }
      var out = function() {
        // this.sprite(0, 0, 1, 1);
      }
      var land = Crafty.e("2D, Canvas, grass, Text, Mouse").attr({
        z: z,
        w: size,
        h: size
      })
      // used to check if the mouse is over the component
      .areaMap([size / 2, 0], [size, size / 4], [size / 2, size / 2],
              [0, size / 4]);
      // on mouse click handler
      land.bind("Click", OnClickCallbackManager.onClick)
      // mouseover handler
      .bind("MouseOver", over).bind("MouseOut", out);

      land.def = {
        type: "grass",
        i: i,
        j: y,
        over: over,
        out: out,
        field: field
      };
    }
    return land;
  }
  ;

  this.init = function() {
    for (var i = 0; i <= columns; i++) {
      for (var y = firstRow; y < rows; y++) {
        var tile = createTile(i, y, atom);
        if (tile) {
          iso.place(i, y, 0, tile);
          if (checkInside(i, y)) {
            iso.place(i, y, 0, Crafty.e("2D, DOM, Text").attr({
              z: 1000,
              w: atom,
              h: atom
            }).css({
              "text-align": "center",
              "padding-top": "28px"
            }).text(i + '-' + y));

          }
          field[i + "-" + y] = tile;
        }
      }
    }
  };

};