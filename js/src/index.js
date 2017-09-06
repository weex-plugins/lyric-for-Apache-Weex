

const WeexLyric = {
  show() {
      alert("module WeexLyric is created sucessfully ")
  }
};


var meta = {
   WeexLyric: [{
    name: 'show',
    args: []
  }]
};



if(window.Vue) {
  weex.registerModule('WeexLyric', WeexLyric);
}

function init(weex) {
  weex.registerApiModule('WeexLyric', WeexLyric, meta);
}
module.exports = {
  init:init
};
