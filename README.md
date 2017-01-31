# BottomNavigation

Material design Bottom Navigation implementation

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-BottomNavigation-blue.svg?style=flat-square)](http://android-arsenal.com/details/1/3330)


```
final BottomNavigation navigation = (BottomNavigation) findViewById(R.id.bottom_navigation);

navigation.setOnItemSelectedListener(new BottomNavigation.OnItemSelectedListener() {
    @Override
    public void itemSelected(int id) {
            // Handle screen change
        }
    });

navigation.builder()
    .items(
          new BottomNavigation.NavigationItem(0, getResources().getDrawable(R.drawable.ic_face_white_24dp), "People", Color.parseColor("#7C4DFF")),
          new BottomNavigation.NavigationItem(1, getResources().getDrawable(R.drawable.ic_computer_white_24dp), "Computers", Color.parseColor("#795548")),
          new BottomNavigation.NavigationItem(2, getResources().getDrawable(R.drawable.ic_build_white_24dp), "Tools", Color.parseColor("#03A9F4")))
    .withColorChanges(true)
    .hideOnScroll(true)
    .startingPosition(1)
    .build();
```          
![alt tag](https://media.giphy.com/media/xThuW13vdvR57YD9Ha/giphy.gif)

## Usage

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.krpiotrek:BottomNavigation:library:d6a02f9686'
}
```

## License

The BottomNavigation library is available under the Apache 2.0 License.
