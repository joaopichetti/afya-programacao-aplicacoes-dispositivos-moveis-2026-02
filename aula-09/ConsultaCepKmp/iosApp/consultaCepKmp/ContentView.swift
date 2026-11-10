//
//  ContentView.swift
//  consultaCepKmp
//
//  Created by João Guilherme Brasil Pichetti on 09/11/26.
//

import SwiftUI
import sharedKit

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hello from shared module: \(Platform_iosKt.platform())")
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
