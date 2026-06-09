// export default ViewChannel
import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import { v4 as uuidv4 } from "uuid";
import Swal from "sweetalert2";
import amsApi from "../../api/amsApi";
import Getaccounttype from "../paginations/Getaccounttype";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function ViewMcc() {
  const [alldata, setAlldata] = useState([]);
  //console.log(alldata);
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");
  //   Pagination
  //   Pagination
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
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
    viewMcc();
  }, []);

  const viewMcc = async () => {
    try {
      const response = await amsApi.post(`mcc_code/getAllMccCode`, {});
      if (response.status === 200) {
        setAlldata(response.data);
        // setTcount(response.data[0].tcount);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

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

  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
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
  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  return (
    <AppLayout>
      <div className="w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Mcc
              </p>
              <div className="flex justify-center rounded-md border border-transparent px-2 mx-2 text-sm font-medium text-white ">
                <div className="ml-4 w-auto  relative mt-1 text-gray-600">
                  {" "}
                  <input
                    type="search"
                    id="search"
                    className="block w-full px-3 py-1  text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    // value={searchKeyword}
                    // onChange={handleSearch}
                    // style={{
                    //   backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    // }}
                    // disabled={checkpoint !== 1}
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-2 mr-2"
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
            </div>
          </div>
        </div>

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={viewMcc}
        />
        <div className="overflow-x-auto relative shadow-md ">
          <>
            <div className="table-wrp block max-h-[28rem] ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-2 px-6 whitespace-nowrap "
                    onClick={() => handleSort("strMccCode")}
                  >
                    MCC CODE {renderSortArrow("strMccCode")}
                  </th>
                  <th
                    scope="col"
                    className="py-2 px-6  whitespace-nowrap "
                    onClick={() => handleSort("strMccCodeDesc")}
                  >
                    MCC Description {renderSortArrow("strMccCodeDesc")}
                  </th>
                </thead>
                <tbody>
                  <>
                    {sortedData.map(({ strMccCode, strMccCodeDesc }) => (
                      <tr
                        key={uuidv4()}
                        className="bg-blue-00 border-b hover:bg-blue-200
                    border-blue-400"
                      >
                        <td className="px-6 py-2 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {strMccCode || "-"}
                          </div>
                        </td>
                        <td className="px-6 py-2 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {strMccCodeDesc || "-"}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </>
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
