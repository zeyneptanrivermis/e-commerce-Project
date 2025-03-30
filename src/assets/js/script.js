function toggleDropdown() {
  document.getElementById("dropdownMenu").classList.toggle("show");
}

// Sayfanın herhangi bir yerine tıklandığında menüyü kapat
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
      let dropdowns = document.getElementsByClassName("dropdown-content");
      for (let i = 0; i < dropdowns.length; i++) {
          let openDropdown = dropdowns[i];
          if (openDropdown.classList.contains('show')) {
              openDropdown.classList.remove('show');
          }
      }
  }
};
