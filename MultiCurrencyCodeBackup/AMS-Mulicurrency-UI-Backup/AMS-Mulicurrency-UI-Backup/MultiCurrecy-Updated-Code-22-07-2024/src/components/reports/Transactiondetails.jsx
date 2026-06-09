import React from "react";
import AppLayout from "../../layout/AppLayout";
import { useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useParams } from "react-router-dom";
import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
import { useNavigate } from "react-router-dom";
// import AutoPagintation from "../../UI/AutoPagintation";
export default function Transactiondetails() {
  const [alldata, setAlldata] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const navigate = useNavigate();
  const { strTxn_id } = useParams();
  console.log(strTxn_id);
  //   Pagination
  //   Pagination

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };

  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const {
      strTxn_id,
      txn_Date,
      txn_Time,
      strTran_type,
      strTransaction_amount,
      strAccountNumber,
      strFrom_account_number,
      strTo_account_number,
      strAuthCode,
      strResponseCode,
    } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (strTxn_id &&
        strTxn_id.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (txn_Date && txn_Date.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (txn_Time && txn_Time.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strTran_type &&
        strTran_type.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strTransaction_amount &&
        strTransaction_amount
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (strAccountNumber &&
        strAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strFrom_account_number &&
        strFrom_account_number
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (strTo_account_number &&
        strTo_account_number.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strAuthCode &&
        strAuthCode.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strResponseCode &&
        strResponseCode.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
  };
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    handleSubmit();
  }, []);

  const handleSubmit = async (e) => {
    try {
      const response = await amsApi.post(
        `accountTxnMaster/searchTransationByTxnId`,
        {
          strTxn_id: strTxn_id,
        },
        {
          headers: {
            "Content-type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountTranMaster);
        // handleShowSuccess(response.data.message);

        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // sorted Table data
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const renderSortArrow = (column) => {
    if (column === sortColumn) {
      return sortOrder === "asc" ? "↑" : "↓";
    }
    return "↕";
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...filteredData].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-6 sm:px-10 rounded-t-lg ">
            <div className=" flex items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Transaction View
              </p>

              <div className="flex justify-end rounded-md border border-transparent px-2 mx-2 text-sm font-normal text-white ">
                {" "}
                <div className="pt-2 relative mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchKeyword}
                    onChange={handleSearch}
                    className="block w-full px-2 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-4 mr-1"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </button>
                </div>
              </div>
              <button
                title="Go back"
                type="button"
                onClick={() => navigate(-1)}
                className="inline-flex justify-center rounded-md border border-transparent bg-gray-500 py-1 px-3  text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
              >
                <ArrowLeftIcon className="h-4 w-4" />
              </button>
            </div>
          </div>
        </div>

        {/* table */}
        <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
          {/* <AutoPagintation
            addPageperData={setItemPage}
            addCurrentPage={setTcount}
            pagePerData={itemPage}
            currentPage={tcount}
            tcount={alldata[0]?.strTotalCount}
            reCallApi={handleSubmit}
          /> */}

          {/* {alldata.length > 0 && (
          <> */}
          <div className="bg-white overflow-x-auto relative shadow-md ">
            <div className="table-wrp block max-h-[27rem] max-w-[27rem]">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100">
                <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <tr>
                    <th
                      scope="col"
                      className="py-2 px-6 whitespace-nowrap"
                      onClick={() => handleSort("strTxn_id")}
                    >
                      TXN ID {renderSortArrow("strTxn_id")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6 whitespace-nowrap "
                      onClick={() => handleSort("txn_Date")}
                    >
                      TXN DATE {renderSortArrow("txn_Date")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("txn_Time")}
                    >
                      TXN TIME {renderSortArrow("txn_Time")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strTran_type")}
                    >
                      TXN TYPE {renderSortArrow("strTran_type")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strTransaction_amount")}
                    >
                      TXN AMOUNT {renderSortArrow("strTransaction_amount")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strAccountNumber")}
                    >
                      A/c NO {renderSortArrow("strAccountNumber")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strFrom_account_number")}
                    >
                      FROM A/c NO {renderSortArrow("strFrom_account_number")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strTo_account_number")}
                    >
                      TO A/c NO {renderSortArrow("strTo_account_number")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strAuthCode")}
                    >
                      AUTH CODE {renderSortArrow("strAuthCode")}
                    </th>
                    <th
                      scope="col"
                      className="py-2 px-6  whitespace-nowrap"
                      onClick={() => handleSort("strResponseCode")}
                    >
                      RESPONSE CODE {renderSortArrow("strResponseCode")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map(
                        ({
                          strTxn_id,
                          txn_Date,
                          txn_Time,
                          strTran_type,
                          strTransaction_amount,
                          strAccountNumber,
                          strFrom_account_number,
                          strTo_account_number,
                          strAuthCode,
                          strResponseCode,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="border-b dark:border-neutral-500"
                          >
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strTxn_id || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  txn_Date || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  txn_Time || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strTran_type || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap text-right">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strTransaction_amount || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strAccountNumber || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strFrom_account_number || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strTo_account_number || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strAuthCode || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {highlightKeyword(
                                  strResponseCode || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className="px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No Data Available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      {/* </div> */}

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
