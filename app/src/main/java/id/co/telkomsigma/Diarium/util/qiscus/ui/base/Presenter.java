package id.co.telkomsigma.Diarium.util.qiscus.ui.base;

/**
 * Created by adicatur on 12/24/16.
 */

public interface Presenter<V extends Mvp> {

    void onAttachView(V view);

    void onDetachView();
}
