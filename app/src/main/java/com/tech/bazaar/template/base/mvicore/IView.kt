package com.tech.bazaar.template.base.mvicore

interface IView<S: IState> {
    fun render(state: S)
}