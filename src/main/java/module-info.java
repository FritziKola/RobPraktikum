module org.openjfx {
    requires transitive javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.base;

    exports externalThings.Jama;
    exports org.openjfx;

}