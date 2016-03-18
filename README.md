# BottomNavigation

Material design Bottom Navigation implementation

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
![alt tag](http://im2.ezgif.com/tmp/ezgif-129172832.gif)
