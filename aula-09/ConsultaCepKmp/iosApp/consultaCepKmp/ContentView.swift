//
//  ContentView.swift
//  consultaCepKmp
//
//  Created by João Guilherme Brasil Pichetti on 09/11/26.
//

import SwiftUI
import UIKit
import sharedKit

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea()
    }
}
