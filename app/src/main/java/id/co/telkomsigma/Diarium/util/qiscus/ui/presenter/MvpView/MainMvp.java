package id.co.telkomsigma.Diarium.util.qiscus.ui.presenter.MvpView;

import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;

import id.co.telkomsigma.Diarium.util.qiscus.ui.base.Mvp;


/**
 * Created by adicatur on 12/24/16.
 */

public interface MainMvp extends Mvp {

    void startChat(QiscusChatRoom qiscusChatRoom);

}
