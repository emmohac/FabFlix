function startTime() {
    let today = new Date();
    let m = checkTime(today.getMinutes());
    let s = checkTime(today.getSeconds());
    document.getElementById('txt').innerHTML = today.getHours() + ":" + m + ":" + s;
    let t = setTimeout(startTime, 500);
  }
  function checkTime(i) {
    return ((i < 10) ? i = "0" + i : i);
  }