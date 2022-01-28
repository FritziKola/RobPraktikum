module org.openjfx {
    requires transitive javafx.controls;
	requires transitive javafx.graphics;

    exports externalThings.Jama;
    exports org.openjfx;

}