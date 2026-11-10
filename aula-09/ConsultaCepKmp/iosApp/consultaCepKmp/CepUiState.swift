import Foundation
import sharedKit

enum CepUiState: Equatable {
    case idle
    case loading
    case success(Endereco)
    case error(String)
}