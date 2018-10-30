/**
 * Created by Ty136 on 2/20/2017.
 */

var data;
var yearSelector = [];

function getData() {
    // var url = "albums/albums_short.json";
    var url = "albums/albums.json";
    var request = new XMLHttpRequest();
    request.open("GET", url, true);
    request.send(null);
    request.onload = function () {
        if (request.status == 200){
            //alert("Data received.")
            data = JSON.parse(request.responseText);
            getMusic();
        }
    }
}

function getMusic() {
    var count = 0;
    if (data) {
        count = data.length;
        document.getElementById("count").innerHTML = '&nbsp;' + count;

        displayMusic(true, true);

        for (var i = 0; i < yearSelector.length; i++){
            var option = document.createElement('option');
            option.innerHTML = yearSelector[i];
            option.value = yearSelector[i];
            document.getElementById('years').appendChild(option);
        }
    }
}

function displayMusic(getAll, getOptions) {
    removeItems();
    var easyRemove = document.createElement('div');
    easyRemove.id = 'easyRemove';
    var optionValue = 'empty';
    if (!getAll){
       var e = document.getElementById("years");
        optionValue = e.options[e.selectedIndex].value;
    }
    for (var i = 0; i < data.length; i++) {
        var item = document.createElement('div');
        item.className = 'item';
        var artist = document.createElement('div');
        artist.innerHTML = data[i].Artist;
        var classification = document.createElement('div');
        classification.innerHTML = data[i].Classification;
        var format = document.createElement('div');
        format.innerHTML = data[i].Format;
        var label = document.createElement('div');
        label.innerHTML = data[i].Label;
        var purchase = document.createElement('div');
        purchase.innerHTML = data[i].PurchaseYear;
        var release = document.createElement('div');
        release.innerHTML = data[i].ReleaseYear;
        if (getOptions){
            if (yearSelector.indexOf(data[i].ReleaseYear) == -1){
                yearSelector.push(data[i].ReleaseYear);
            }
        }
        var title = document.createElement('div');
        title.innerHTML = data[i].Title;

        if (!getAll){
            if (optionValue == data[i].ReleaseYear) {
                item.appendChild(artist);
                item.appendChild(classification);
                item.appendChild(format);
                item.appendChild(label);
                item.appendChild(purchase);
                item.appendChild(release);
                item.appendChild(title);

                easyRemove.appendChild(item);
            }
        } else {
            item.appendChild(artist);
            item.appendChild(classification);
            item.appendChild(format);
            item.appendChild(label);
            item.appendChild(purchase);
            item.appendChild(release);
            item.appendChild(title);

            easyRemove.appendChild(item);
        }
    }
    if (getOptions){
        yearSelector.sort();
    }
    document.body.appendChild(easyRemove);
}

function removeItems() {
    document.getElementById('easyRemove').remove();
}
